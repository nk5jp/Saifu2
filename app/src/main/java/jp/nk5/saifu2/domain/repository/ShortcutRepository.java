package jp.nk5.saifu2.domain.repository;

import jp.nk5.saifu2.domain.Shortcut;

public interface ShortcutRepository {

    void setShortcut(String name, int boxId, int typeId, int firstId, int secondId, int value) throws Exception;
    Shortcut getShortcutByBoxId(int boxId) throws Exception;
    void deleteShortcutByBoxId(int boxId) throws Exception;

}
