package aunmag.shooter.environment.utils;

import aunmag.nightingale.utilities.UtilsMath;
import aunmag.shooter.environment.World;
import aunmag.shooter.environment.decorations.Decoration;
import aunmag.shooter.environment.decorations.DecorationType;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public class TreesGenerator {

    private final World world;
    private final float size;
    private final float treesQuantity;
    private final float intervalMin;

    public TreesGenerator(
            World world,
            float size,
            float treesQuantityPerMeter,
            float intervalMin
    ) {
        this.world = world;
        this.size = size;
        this.treesQuantity = size * size * treesQuantityPerMeter;
        this.intervalMin = intervalMin;
    }

    public void generate() {
        for (int i = 0; i < treesQuantity; i++) {
            Decoration tree = tryGenerateTree();
            if (tree != null) {
                world.getTrees().all.add(tree);
            }
        }
    }

    @Nullable
    private Decoration tryGenerateTree() {
        @Nullable Vector2f position = null;

        for (int attempt = 0; attempt < 32; attempt++) {
            position = generatePosition();

            if (checkIsPositionUnoccupied(position)) {
                break;
            }

            position = null;
        }

        if (position != null) {
            return new Decoration(
                    generateType(),
                    position.x,
                    position.y,
                    generateRadians()
            );
        } else {
            return null;
        }
    }

    private Vector2f generatePosition() {
        float sizeHalf = size / 2f;
        float x = UtilsMath.randomizeBetween(-sizeHalf, sizeHalf);
        float y = UtilsMath.randomizeBetween(-sizeHalf, sizeHalf);
        return new Vector2f(x, y);
    }

    private float generateRadians() {
        return UtilsMath.randomizeBetween(0, (float) UtilsMath.PIx2);
    }

    private DecorationType generateType() {
        int number = UtilsMath.random.nextInt(3) + 1;

        if (number == 1) {
            return DecorationType.tree1;
        } else if (number == 2) {
            return DecorationType.tree2;
        } else {
            return DecorationType.tree3;
        }
    }

    private boolean checkIsPositionUnoccupied(Vector2f position) {
        for (Decoration tree: world.getTrees().all) {
            float intervalX = Math.abs(position.x - tree.body.position.x);
            float intervalY = Math.abs(position.y - tree.body.position.y);
            if (intervalX < intervalMin && intervalY < intervalMin) {
                return false;
            }
        }

        return true;
    }

}
