package jp.nk5.saifu2.domain.repository;

import jp.nk5.saifu2.domain.Template;

public interface CostRepository {

    Template getTemplateById(int id);

    int calculateEstimate(int id);

}
