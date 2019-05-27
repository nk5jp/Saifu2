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
import jp.nk5.saifu2.domain.Template;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.view.viewmodel.CreatingTemplateViewModel;

public class CreatingTemplateFragment extends Fragment {

    private CreatingTemplateViewModel viewModel = new CreatingTemplateViewModel(
            new Template(
                    SpecificId.NotPersisted.getId(),
                    "",
                    false,
                    true
            )
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
        viewModel.setTemplate(
                new Template(
                        SpecificId.NotPersisted.getId(),
                        "",
                        false,
                        true
                )
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        layout = inflater.inflate(R.layout.fragment_creating_template, container, false);
        return layout;
    }

    public void updateView()
    {
        Template template = viewModel.getTemplate();

        EditText editText = layout.findViewById(R.id.editText1);
        editText.setText(template.getName());

        CheckBox checkBox = layout.findViewById(R.id.checkBox1);
        checkBox.setChecked(template.isControlled());

        Button button = layout.findViewById(R.id.button1);
        if (template.getId() == SpecificId.MeansNull.getId()) button.setText(R.string.str_add_template);
        else button.setText(R.string.str_edit_template);

    }


}
