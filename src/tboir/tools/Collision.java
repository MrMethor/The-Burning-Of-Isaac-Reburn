package tboir.tools;

import tboir.entities.Entity;
import tboir.engine.Side;

public record Collision(Side side, double penetration, Entity entity) {
}
