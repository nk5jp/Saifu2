package jp.nk5.saifu2.domain.repository;

import java.util.List;

import jp.nk5.saifu2.domain.Template;

public interface CostRepository {

    void setTemplate(String name, boolean isControlled) throws Exception;
    void updateTemplate(int id, String name, boolean isControlled) throws  Exception;
    void validInvalidTemplate(int id) throws Exception;
    List<Template> getAllTemplate() throws Exception;
    Template getTemplateById(int id) throws Exception;

    int calculateEstimate(int id);

}
