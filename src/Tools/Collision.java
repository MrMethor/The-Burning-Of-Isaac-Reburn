package Tools;

import Enums.EntityType;
import Enums.Side;

public record Collision(EntityType entityType, Side side, double penetration) {
}
