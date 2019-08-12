package jp.nk5.saifu2.view.viewmodel;

import java.util.List;

import jp.nk5.saifu2.domain.Cost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class CostViewModel {

    @Getter @Setter
    int totalExpected;
    @Getter @Setter
    int totalResult;
    @Getter @Setter
    List<Cost> costs;

}
