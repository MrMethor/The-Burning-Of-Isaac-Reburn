package Tools;

import Entities.Entity;
import Enums.Side;

public record Collision(Side side, double penetration, Entity entity) {
}
