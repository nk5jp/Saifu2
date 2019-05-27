package jp.nk5.saifu2.infra.repository;

import android.content.Context;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import jp.nk5.saifu2.domain.Cost;
import jp.nk5.saifu2.domain.repository.CostRepository;
import jp.nk5.saifu2.domain.Template;
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
        templates = templateDAO.readAll();
        costs = costDAO.readAll();
    }

    public void setTemplate(String name, boolean isControlled) throws Exception
    {
        Template template = new Template(SpecificId.NotPersisted.getId(), name, isControlled, true);
        templateDAO.createTemplate(template);
        templates.add(template);
    }

    public void openCloseTemplate(int id) throws Exception
    {
        Template template = getTemplateById(id);
        template.setValid(!template.isValid());
        templateDAO.updateTemplate(template);
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

    public int calculateEstimate(int id)
    {
        return 0;
    }



}
