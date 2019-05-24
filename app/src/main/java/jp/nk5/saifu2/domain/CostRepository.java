package jp.nk5.saifu2.domain;

public interface CostRepository {

    Template getTemplateById(int id);

    int calculateEstimate(int id);

}
