package jp.nk5.saifu2.infra.repository;

import android.content.Context;

import jp.nk5.saifu2.domain.Shortcut;
import jp.nk5.saifu2.domain.repository.ShortcutRepository;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.infra.dao.ShortcutDAO;

public class ShortcutRepositorySQLite implements ShortcutRepository {

    private Shortcut[] shortcuts;
    private static ShortcutRepositorySQLite instance;
    private ShortcutDAO shortcutDAO;

    public static ShortcutRepositorySQLite getInstance(Context context) throws Exception
    {
        if (instance == null)
        {
            instance = new ShortcutRepositorySQLite(context);
        }
        return instance;
    }

    private ShortcutRepositorySQLite(Context context) throws Exception
    {
        this.shortcutDAO = new ShortcutDAO(context);
        shortcuts = new Shortcut[] {
                shortcutDAO.readShortcutByBoxId(0),
                shortcutDAO.readShortcutByBoxId(1),
                shortcutDAO.readShortcutByBoxId(2),
                shortcutDAO.readShortcutByBoxId(3),
                shortcutDAO.readShortcutByBoxId(4),
                shortcutDAO.readShortcutByBoxId(5)
        };
    }

    @Override
    public void setShortcut(String name, int boxId, int typeId, int firstId, int secondId, int value) throws Exception {
        Shortcut shortcut = new Shortcut(
                SpecificId.NotPersisted.getId(),
                name,
                boxId,
                typeId,
                firstId,
                secondId,
                value
        );

        if (shortcuts[boxId] == null) shortcutDAO.createShortcut(shortcut);
        else shortcutDAO.updateShortcutByBoxId(shortcut);

        shortcuts[boxId] = shortcut;
    }

    @Override
    public Shortcut getShortcutByBoxId(int boxId) {
        return shortcuts[boxId];
    }

    @Override
    public void deleteShortcutByBoxId(int boxId) throws Exception {
        shortcutDAO.deleteShortcutByBoxId(boxId);
        shortcuts[boxId] = null;
    }
}
