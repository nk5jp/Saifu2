package jp.nk5.saifu2.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import jp.nk5.saifu2.R;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.view.viewmodel.CreatingTemplateViewModel;

public class CreatingTemplateFragment extends Fragment {

    private CreatingTemplateViewModel viewModel = new CreatingTemplateViewModel(
            SpecificId.NotPersisted.getId(),
            "",
            false
    );
    private View layout;

    public static String getTagName()
    {
        return "TAG_CREATING_TEMPLATE";
    }

    public CreatingTemplateViewModel getViewModel() {
        return this.viewModel;
    }

    /**
     * replaceのたびに画面モデルを初期化する
     */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        initializeViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        layout = inflater.inflate(R.layout.fragment_creating_template, container, false);
        return layout;
    }

    public void updateView()
    {
        EditText editText = layout.findViewById(R.id.editText1);
        editText.setText(viewModel.getName());

        CheckBox checkBox = layout.findViewById(R.id.checkBox1);
        checkBox.setChecked(viewModel.isControlled());

        Button button = layout.findViewById(R.id.button1);
        if (viewModel.getId() == SpecificId.NotPersisted.getId()) button.setText(R.string.str_add_template);
        else button.setText(R.string.str_edit_template);
    }

    /**
     * 各情報をクリアする，画面表示まで行う
     */
    public void clearView()
    {
        initializeViewModel();
        updateView();
    }

    /**
     * こちらは初期化処理のみ行う
     */
    private void initializeViewModel()
    {
        viewModel.setId(SpecificId.NotPersisted.getId());
        viewModel.setName("");
        viewModel.setControlled(false);
    }

}
