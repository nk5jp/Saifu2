package jp.nk5.saifu2.service;

import android.content.Context;

import jp.nk5.saifu2.BankActivity;
import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.repository.AccountRepository;
import jp.nk5.saifu2.infra.repository.AccountRepositorySQLite;
import jp.nk5.saifu2.view.fragment.AccountFragment;
import jp.nk5.saifu2.view.fragment.TransferFragment;
import jp.nk5.saifu2.view.viewmodel.AccountViewModel;
import jp.nk5.saifu2.view.viewmodel.TransferViewModel;

/**
 * 入金や振替処理に関する処理を担う．
 */
public class TransferService {

    private BankActivity errorListener;
    private TransferFragment updateFormViewListener;
    private AccountFragment updateInfoViewListener;
    private AccountRepository accountRepository;

    /**
     *
     * @param context 依頼元のアクティビティ．リポジトリの初期化の都合上必要であり本クラスでは使用しない．
     * @param updateFormViewListener 画面モデルおよび表示更新の依頼先．実態は依頼元のフラグメント．
     * @param updateInfoViewListener 画面モデルおよび表示更新の依頼先．実態は依頼元のフラグメント．
     * @param errorListener エラー表示の依頼先．実態は依頼元のアクティビティ．
     * @throws Exception 初期化にかかる各種エラーが発生した場合
     */
    public TransferService(Context context, TransferFragment updateFormViewListener, AccountFragment updateInfoViewListener, BankActivity errorListener) throws Exception
    {
        accountRepository = AccountRepositorySQLite.getInstance(context);
        this.updateFormViewListener = updateFormViewListener;
        this.updateInfoViewListener = updateInfoViewListener;
        this.errorListener = errorListener;
    }

    /**
     * 描画処理．指定したIDに対応する口座の選択処理を行う．
     * Debit -> Creditの順に画面モデルにセットされる．両方セットされている場合は何もしない．
     * 画面モデル更新後，表示をアップデートする．
     * @param id 対象とする口座ID
     */
    public void setTransferAccount(int id)
    {
        try {
            Account targetAccount = accountRepository.getAccount(id);
            if (!targetAccount.isOpened()) return;

            TransferViewModel viewModel = updateFormViewListener.getViewModel();

            if (viewModel.getDebit() == null) viewModel.setDebit(targetAccount);
            else if (viewModel.getCredit() == null) viewModel.setCredit(targetAccount);

            updateFormViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    /**
     * 画面モデルをnullにして表示を更新する．
     */
    public void resetTransferAccount()
    {
        TransferViewModel viewModel = updateFormViewListener.getViewModel();

        viewModel.setCredit(null);
        viewModel.setDebit(null);
        updateFormViewListener.updateView();
    }

    /**
     * 振替もしくは入金処理を行う
     * @param value 金額
     */
    public void transferMoney(int value)
    {
        try {
            TransferViewModel formViewModel = updateFormViewListener.getViewModel();

            if (isDepositCase(formViewModel)) {
                int debitId = formViewModel.getDebit().getId();
                accountRepository.depositMoney(debitId, value);
            }

            if (isTransferCase(formViewModel)) {
                int debitId = formViewModel.getDebit().getId();
                int creditId = formViewModel.getCredit().getId();
                accountRepository.transferMoney(debitId, creditId, value);
            }

            resetTransferAccount();
            AccountViewModel infoViewModel = updateInfoViewListener.getViewModel();
            infoViewModel.setAccounts(accountRepository.getAllAccount());
            updateInfoViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    /**
     * 現状の画面モデルが入金モードであることを確認する
     * @param viewModel 確認対象とする画面モデル
     * @return 入金ケースである
     */
    private boolean isDepositCase(TransferViewModel viewModel)
    {
        return viewModel.getDebit() != null && viewModel.getCredit() == null;
    }

    /**
     * 現状の画面モデルが振替モードであることを確認する
     * @param viewModel 確認対象とする画面モデル
     * @return 振替ケースである

     */
    private boolean isTransferCase(TransferViewModel viewModel)
    {
        return viewModel.getDebit() != null && viewModel.getCredit() != null;
    }

}
