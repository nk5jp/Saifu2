package jp.nk5.saifu2.infra.repository;

import android.content.Context;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.nk5.saifu2.domain.Cost;
import jp.nk5.saifu2.domain.ExtraCost;
import jp.nk5.saifu2.domain.NormalCost;
import jp.nk5.saifu2.domain.UnControlledCost;
import jp.nk5.saifu2.domain.repository.CostRepository;
import jp.nk5.saifu2.domain.Template;
import jp.nk5.saifu2.domain.util.MyDate;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.infra.dao.CostDAO;
import jp.nk5.saifu2.infra.dao.TemplateDAO;

public class CostRepositorySQLite implements CostRepository {

    private static CostRepositorySQLite instance;
    private TemplateDAO templateDAO;
    private CostDAO costDAO;
    private List<Template> templates;
    private List<Cost> costs;

    public static CostRepositorySQLite getInstance(Context context) throws Exception
    {
        if (instance == null)
        {
            instance = new CostRepositorySQLite(context);
        }
        return instance;
    }

    private CostRepositorySQLite(Context context) throws Exception
    {
        templateDAO = new TemplateDAO(context);
        costDAO = new CostDAO(context, this);
        initialize();
    }

    public void initialize() throws Exception
    {
        templates = templateDAO.readAll();
        costs = costDAO.readAll();
    }

    public void setTemplate(String name, boolean isControlled) throws Exception
    {
        Template template = new Template(SpecificId.NotPersisted.getId(), name, isControlled, true);
        templateDAO.createTemplate(template);
        templates.add(template);
    }

    public List<Template> getAllTemplate()
    {
        return templates;
    }


    public Template getTemplateById(int id)
    {
        Optional<Template> optional = templates.stream()
                .filter(a -> a.getId() == id)
                .findFirst();
        if (optional.isPresent())
        {
            return optional.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    public void updateTemplate(int id, String name, boolean isControlled) throws Exception
    {
        Template template = getTemplateById(id);
        template.setName(name);
        template.setControlled(isControlled);
        templateDAO.updateTemplate(template);
    }

    public void validInvalidTemplate(int id) throws Exception
    {
        Template template = getTemplateById(id);
        template.setValid(!template.isValid());
        templateDAO.updateTemplate(template);
    }

    public void setCostFromTemplate(int year, int month, Template template) throws Exception
    {
        Cost cost;
        int templateId = template.getId();
        if (template.isControlled()) {
            cost = new NormalCost(SpecificId.NotPersisted.getId(),
                    calculateEstimate(templateId),
                    0,
                    true,
                    new MyDate(year, month),
                    template
            );
        } else {
            cost = new UnControlledCost(
                    SpecificId.NotPersisted.getId(),
                    0,
                    true,
                    new MyDate(year, month),
                    template
            );
        }
        costDAO.createCost(cost);
        costs.add(cost);
    }

    public void setCostFromExtra(int year, int month, String name, int estimate) throws Exception
    {
        Cost cost;
        cost = new ExtraCost(SpecificId.NotPersisted.getId(),
                estimate,
                0,
                name,
                true,
                new MyDate(year, month)
        );
        costDAO.createCost(cost);
        costs.add(cost);
    }


    @Override
    public List<Cost> getSpecificCost(int year, int month)
    {
        return costs.stream()
                .filter(t -> t.getDate().getYear() == year && t.getDate().getMonth() == month)
                .collect(Collectors.toList());
    }

    @Override
    public Cost getCostById(int id)
    {
        Optional<Cost> optional = costs.stream()
                .filter(a -> a.getId() == id)
                .findFirst();
        if (optional.isPresent())
        {
            return optional.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    public void validInvalidCost(int id) throws Exception
    {
        Cost cost = getCostById(id);
        cost.setValid(!cost.isValid());
        costDAO.updateCost(cost);
    }

    public int calculateEstimate(int id) throws Exception
    {
        List<Cost> latestCosts = costDAO.readByIdLatestThree(id);
        return (int) latestCosts.stream()
                .mapToInt(Cost::getResult)
                .average().orElse(0);
    }

}
