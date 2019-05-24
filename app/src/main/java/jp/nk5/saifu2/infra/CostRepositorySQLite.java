package jp.nk5.saifu2.infra;

import android.content.Context;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import jp.nk5.saifu2.domain.Cost;
import jp.nk5.saifu2.domain.CostRepository;
import jp.nk5.saifu2.domain.Template;

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
