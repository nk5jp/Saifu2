package jp.nk5.saifu2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import jp.nk5.saifu2.adapter.AccountSpinnerAdapter;
import jp.nk5.saifu2.adapter.CostSpinnerAdapter;
import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.Cost;
import jp.nk5.saifu2.domain.Shortcut;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.service.CreatingShortcutService;

public class ShortcutActivity extends BaseActivity implements Spinner.OnItemSelectedListener {

    private int boxId;
    private CreatingShortcutService creatingShortcutService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut);

        Intent intent = getIntent();
        boxId = intent.getIntExtra("boxId", -1);
        if (boxId == -1) finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            creatingShortcutService = new CreatingShortcutService(this);
            Spinner typeSpinner = findViewById(R.id.spinner1);
            typeSpinner.setOnItemSelectedListener(this);
            setScreen();
        } catch (Exception e) {
            finish();
        }
    }

    private void setScreen() throws Exception
    {
        Spinner typeSpinner = findViewById(R.id.spinner1);
        Spinner firstSpinner = findViewById(R.id.spinner2);
        Spinner secondSpinner = findViewById(R.id.spinner3);

        Shortcut shortcut = creatingShortcutService.getShortcut(boxId);
        firstSpinner.setAdapter((new AccountSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, creatingShortcutService.getValidAccount())));

        if (shortcut == null || shortcut.getTypeId() == SpecificId.Shop.getId())
        {
            typeSpinner.setSelection(0);
            secondSpinner.setAdapter((new CostSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, creatingShortcutService.getValidCost())));
        } else {
            typeSpinner.setSelection(1);
            secondSpinner.setAdapter((new AccountSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, creatingShortcutService.getValidAccount())));
        }

        if (shortcut != null) {
            EditText editText = findViewById(R.id.editText1);
            editText.setText(shortcut.getName());
        }

    }


    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        Spinner typeSpinner = findViewById(R.id.spinner1);
        Spinner secondSpinner = findViewById(R.id.spinner3);

        String typeName = typeSpinner.getSelectedItem().toString();
        try {
            if (typeName.equals("Shop"))
                secondSpinner.setAdapter((new CostSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, creatingShortcutService.getValidCost())));
            if (typeName.equals("Transfer"))
                secondSpinner.setAdapter((new AccountSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, creatingShortcutService.getValidAccount())));
        } catch (Exception e) {
            finish();
        }
    }

    public void onNothingSelected(AdapterView parent) {
    }


    public void onClickAddButton(View view)
    {
        try {
            Spinner typeSpinner = findViewById(R.id.spinner1);
            Spinner firstSpinner = findViewById(R.id.spinner2);
            Spinner secondSpinner = findViewById(R.id.spinner3);

            String typeName = typeSpinner.getSelectedItem().toString();
            int firstId = ((Account) firstSpinner.getSelectedItem()).getId();
            int secondId;
            int typeId;
            if (typeName.equals("Shop")) {
                typeId = SpecificId.Shop.getId();
                secondId = ((Cost) secondSpinner.getSelectedItem()).getId();
            } else if (typeName.equals("Transfer")) {
                typeId = SpecificId.Transfer.getId();
                secondId = ((Account) secondSpinner.getSelectedItem()).getId();
            } else {
                throw new Exception();
            }

            String name = getStringFromEditText(R.id.editText1);
            int value = getIntFromEditText(R.id.editText2);

            creatingShortcutService.createShortcut(name, boxId, typeId, firstId, secondId, value);
        } catch (Exception e) {
            showError("cannot create error");
        }
        finish();
    }

    public void onClickDeleteButton(View view)
    {
        try {
            creatingShortcutService.deleteShortcut(boxId);
        } catch (Exception e) {
            showError("cannot deleted");
        }
        finish();
    }


}
