package server.model;

import java.util.Collection;

import model.GameData;

public record ListGamesResponse(Collection<GameData> games) {
}
