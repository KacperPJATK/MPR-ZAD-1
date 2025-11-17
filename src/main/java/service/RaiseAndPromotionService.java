package service;

import model.Position;

public interface RaiseAndPromotionService {
    void promote(String email, Position position);

    void giveRaise(String email, int percent);
}
