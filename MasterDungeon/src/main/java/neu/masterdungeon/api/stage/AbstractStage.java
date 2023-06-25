package neu.masterdungeon.api.stage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class AbstractStage implements Stage {

    private StageType stageType;
    private String stageName;
    private int time;

    @Override
    public StageType getType() {
        return this.stageType;
    }

    @Override
    public String getName() {
        return this.stageName;
    }

    @Override
    public int getTime() {
        return this.time;
    }
}
