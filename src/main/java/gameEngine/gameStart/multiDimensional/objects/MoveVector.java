package gameEngine.gameStart.multiDimensional.objects;

//移动向量
public class MoveVector extends Vector{
    public MoveVector(double... coordinates) {
        super(coordinates);
    }

    public MoveVector(int dimension) {
        super(dimension);
    }
}
