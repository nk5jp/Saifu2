package jp.nk5.saifu2.view.viewmodel;

import java.util.ArrayList;
import java.util.List;

import jp.nk5.saifu2.domain.Template;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class TemplateViewModel {

    @Getter @Setter
    private List<TemplateForView> templates;

    /**
     * テンプレートのエンティティを，画面モデル仕様に変換した上で追加する．
     */
    public void transformAndSetTemplates(List<Template> templateForDomain)
    {
        templates = new ArrayList<>();
        for (Template template : templateForDomain)
        {
            templates.add(
                    new TemplateForView(
                            false,
                            template
                    )
            );
        }
    }


    @AllArgsConstructor
    public class TemplateForView {
        @Getter @Setter
        private boolean isSelected;
        @Getter @Setter
        private Template template;
    }
}
