package jp.nk5.saifu2.view.viewmodel;

import java.util.ArrayList;
import java.util.List;

import jp.nk5.saifu2.domain.Cost;
import jp.nk5.saifu2.domain.util.MyDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ReceiptDetailViewModel {

    @Getter @Setter
    private int id;
    @Getter @Setter
    private MyDate date;
    @Getter @Setter
    private int sum;
    @Getter @Setter
    private List<ReceiptDetailForView> receiptDetails;
    @Getter @Setter
    private MyMode mode;

    @AllArgsConstructor
    public class ReceiptDetailForView
    {
        @Getter @Setter
        private Cost cost;
        @Getter @Setter
        private int value;
        @Getter @Setter
        private boolean isSelected;
    }

    public enum MyMode
    {
        ADD, DELETE
    }

    /**
     * 内部保持している明細のリストに新要素を追加する．合計値の加算も行う．
     * @param cost 追加する明細に紐づく費目
     * @param value 価格
     */
    public void addDetail(Cost cost, int value)
    {
        this.receiptDetails.add(
                new ReceiptDetailForView(cost, value, false)
        );
        sum = sum + value;
    }

    /**
     * 指定した行番号に対応づく明細を削除する．合計値の減算も行う．
     * @param position 削除対象とする明細の行番号
     */
    public void deleteDetail(int position)
    {
        sum = sum - receiptDetails.get(position).getValue();
        receiptDetails.remove(position);
    }

    /**
     * 選択されている明細のみを抽出して返す
     * @return 選択されている明細のリスト
     */
    public List<ReceiptDetailForView> getValidDetail()
    {
        List<ReceiptDetailForView> list = new ArrayList<>();
        for (ReceiptDetailForView detail : receiptDetails)
        {
            if (detail.isSelected()) list.add(detail);
        }
        return list;
    }

}
