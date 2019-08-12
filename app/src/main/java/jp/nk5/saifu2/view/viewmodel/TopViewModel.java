package jp.nk5.saifu2.view.viewmodel;

import jp.nk5.saifu2.domain.Shortcut;
import lombok.Getter;
import lombok.Setter;

public class TopViewModel {

    @Getter @Setter
    private Shortcut[] shortcuts;

    public TopViewModel()
    {
        shortcuts = new Shortcut[6];
    }
}
