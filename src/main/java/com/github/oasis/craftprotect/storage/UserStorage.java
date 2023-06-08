package com.github.oasis.craftprotect.storage;

import com.github.oasis.craftprotect.api.CraftProtectUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class UserStorage {

    private final Supplier<Connection> connectionSupplier;

    public UserStorage(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;

        try (Connection connection = connectionSupplier.get()) {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (id VARCHAR(255), `twitch-id` VARCHAR(255), `discord-id` VARCHAR(255), PRIMARY KEY (id));");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void persist(CraftProtectUser user) {
        try (Connection connection = connectionSupplier.get()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `twitch-id`=?, `discord-id`=?;");
            statement.setString(1, user.getId().toString());
            statement.setString(2, user.getTwitchId());
            statement.setString(3, user.getDiscordId());
            statement.setString(4, user.getTwitchId());
            statement.setString(5, user.getDiscordId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Optional<CraftProtectUser> findUser(UUID id) {
        try (Connection connection = connectionSupplier.get()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id=?;");
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
                return Optional.empty();
            return Optional.of(parse(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Stream<CraftProtectUser> getUsers() {
        try (Connection connection = connectionSupplier.get()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users;");
            ResultSet resultSet = statement.executeQuery();
            int fetchSize = resultSet.getFetchSize();
            List<CraftProtectUser> users = new ArrayList<>(fetchSize);
            while (resultSet.next()) {
                users.add(parse(resultSet));
            }
            return users.stream();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }

    public void delete(CraftProtectUser user) {
        delete(user.getId());
    }

    public void delete(UUID id) {
        try (Connection connection = connectionSupplier.get()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id=?;");
            statement.setString(1, id.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private CraftProtectUser parse(ResultSet resultSet) throws SQLException {
        UUID id = UUID.fromString(resultSet.getString("id"));
        String twitchId = resultSet.getString("twitch-id");
        String discordId = resultSet.getString("discord-id");
        return new CraftProtectUser(id, twitchId, discordId);
    }
}
