package net.blay09.mods.refinedrelocation.util;


import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


public enum RelativeSide {
    BACK,
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
    FRONT;

    @OnlyIn( Dist.CLIENT )
    public static RelativeSide fromFacing( Direction baseFacing, Direction facing ) {
        if( baseFacing == facing ) {
            return FRONT;
        }
        else if( baseFacing.getOpposite() == facing ) {
            return BACK;
        }

        if( baseFacing.getAxis() == Direction.Axis.Y ) {
            Matrix4f matrix = new Matrix4f();
            matrix.multiply( new Quaternion( 90f, 0f, 0f, true ) );
            Direction rot = Direction.rotateFace( matrix, baseFacing );
            if( rot == facing ) {
                return BOTTOM;
            }
            else if( rot == facing.getOpposite() ) {
                return TOP;
            }
        }
        else {
            if( facing == Direction.UP ) {
                return TOP;
            }
            else if( facing == Direction.DOWN ) {
                return BOTTOM;
            }
        }

        if( baseFacing.getAxis() == Direction.Axis.Y ) {
            Matrix4f matrix = new Matrix4f();
            matrix.multiply( new Quaternion( 0f, 0f, 90f, true ) );
            Direction rot = Direction.rotateFace( matrix, baseFacing );
            if( rot == facing ) {
                return baseFacing.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? RIGHT : LEFT;
            }
            else if( rot == facing.getOpposite() ) {
                return baseFacing.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? LEFT : RIGHT;
            }
        }
        else {
            if( baseFacing.rotateY() == facing ) {
                return LEFT;
            }
            else if( baseFacing.rotateYCCW() == facing ) {
                return RIGHT;
            }
        }

        return FRONT;
    }

    private static final RelativeSide[] values = values();

    public static RelativeSide fromIndex( int i ) {
        return values[Math.abs( i % values.length )];
    }

    public Direction toFacing( Direction baseFacing ) {
        switch( this ) {
            case FRONT:
                return baseFacing;
            case BACK:
                return baseFacing.getOpposite();
            case LEFT:
                if( baseFacing.getAxis() == Direction.Axis.Y ) {
                    return Direction.WEST;
                }
                else {
                    return baseFacing.rotateY();
                }
            case RIGHT:
                if( baseFacing.getAxis() == Direction.Axis.Y ) {
                    return Direction.EAST;
                }
                else {
                    return baseFacing.rotateYCCW();
                }
            case TOP:
                if( baseFacing == Direction.UP ) {
                    return Direction.SOUTH;
                }
                else if( baseFacing == Direction.DOWN ) {
                    return Direction.NORTH;
                }
                else {
                    return Direction.UP;
                }
            case BOTTOM:
                if( baseFacing == Direction.UP ) {
                    return Direction.NORTH;
                }
                else if( baseFacing == Direction.DOWN ) {
                    return Direction.SOUTH;
                }
                else {
                    return Direction.DOWN;
                }
        }
        return null;
    }

    public RelativeSide getOpposite() {
        switch( this ) {
            case BACK:
                return FRONT;
            case FRONT:
                return BACK;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case TOP:
                return BOTTOM;
            case BOTTOM:
                return TOP;
        }
        return this;
    }

    @SuppressWarnings( "SuspiciousNameCombination" )
    public RelativeSide rotateY() {
        switch( this ) {
            case BACK:
                return RIGHT;
            case RIGHT:
                return FRONT;
            case FRONT:
                return LEFT;
            case LEFT:
                return BACK;
        }
        return this;
    }

    @SuppressWarnings( "SuspiciousNameCombination" )
    public RelativeSide rotateX() {
        switch( this ) {
            case TOP:
                return LEFT;
            case BOTTOM:
                return RIGHT;
            case RIGHT:
                return BOTTOM;
            case LEFT:
                return TOP;
        }
        return this;
    }

    public RelativeSide rotateZ() {
        switch( this ) {
            case TOP:
                return BACK;
            case BOTTOM:
                return FRONT;
            case BACK:
                return TOP;
            case FRONT:
                return BOTTOM;
        }
        return this;
    }
}
