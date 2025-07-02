package com.ecommerce.ClienteModule.dao.implement;

import com.ecommerce.ClienteModule.dao.IDAO;
import com.ecommerce.ClienteModule.domain.Bandeira;
import com.ecommerce.ClienteModule.domain.Cartao;
import com.ecommerce.ClienteModule.domain.Cliente;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CartaoDAO implements IDAO<Cartao> {

    private final String url = "jdbc:h2:file:./data/meubanco";
    private final String user = "sa";
    private final String password = "";

    public CartaoDAO() {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS bandeira (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, descricao VARCHAR(100) UNIQUE)");

            stmt.execute("CREATE TABLE IF NOT EXISTS cartao (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, num_cartao VARCHAR(30), nome_impresso VARCHAR(100), " +
                    "cod_seguranca VARCHAR(10), bandeira_id BIGINT, cliente_id BIGINT, " +
                    "FOREIGN KEY (bandeira_id) REFERENCES bandeira(id), " +
                    "FOREIGN KEY (cliente_id) REFERENCES cliente(id))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Cartao cartao) {
        saveBandeira(cartao.getBandeira());
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO cartao (num_cartao, nome_impresso, cod_seguranca, bandeira_id, cliente_id) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cartao.getNumCartao());
            ps.setString(2, cartao.getNomeImpresso());
            ps.setString(3, cartao.getCodSeguranca());
            ps.setLong(4, cartao.getBandeira().getId());
            ps.setLong(5, cartao.getCliente().getId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                cartao.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Cartao cartao) {
        String sql = "UPDATE cartao SET num_cartao = ?, nome_impresso = ?, cod_seguranca = ?, bandeira_id = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            saveBandeira(cartao.getBandeira());
            ps.setString(1, cartao.getNumCartao());
            ps.setString(2, cartao.getNomeImpresso());
            ps.setString(3, cartao.getCodSeguranca());
            ps.setObject(4, cartao.getBandeira() != null ? cartao.getBandeira().getId() : null, Types.BIGINT);
            ps.setLong(5, cartao.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cartao findById(Long id) {
        String sql = "SELECT c.*, b.descricao AS bandeira_nome FROM cartao c " +
                "LEFT JOIN bandeira b ON c.bandeira_id = b.id WHERE c.id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Bandeira b = new Bandeira(rs.getString("bandeira_nome"));
                b.setId(rs.getLong("bandeira_id"));

                Cartao c = new Cartao(
                        rs.getString("num_cartao"),
                        rs.getString("nome_impresso"),
                        rs.getString("cod_seguranca"),
                        b,
                        null
                );
                c.setId(rs.getLong("id"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Cartao> findAll() {
        List<Cartao> cartoes = new ArrayList<>();
        String sql = "SELECT c.*, b.descricao AS bandeira_nome FROM cartao c " +
                "LEFT JOIN bandeira b ON c.bandeira_id = b.id";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Bandeira b = new Bandeira(rs.getString("bandeira_nome"));
                b.setId(rs.getLong("bandeira_id"));
                Cartao c = new Cartao(
                        rs.getString("num_cartao"),
                        rs.getString("nome_impresso"),
                        rs.getString("cod_seguranca"),
                        b,
                        null
                );
                c.setId(rs.getLong("id"));
                cartoes.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartoes;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM cartao WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteByClienteId(Long clienteId) {
        String sql = "DELETE FROM cartao WHERE cliente_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, clienteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveBandeira(Bandeira b) {
        if (b == null) return;
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM bandeira WHERE descricao = ?")) {
            ps.setString(1, b.getDescricao());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                b.setId(rs.getLong("id"));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO bandeira (descricao) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.getDescricao());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) b.setId(rs.getLong(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cartao> buscarCartoesPorClienteId(Long clienteId) {
        List<Cartao> list = new ArrayList<>();
        String sql = "SELECT c.*, b.descricao AS bandeira_nome FROM cartao c " +
                "LEFT JOIN bandeira b ON c.bandeira_id = b.id WHERE cliente_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, clienteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Bandeira b = new Bandeira(rs.getString("bandeira_nome"));
                b.setId(rs.getLong("bandeira_id"));
                Cartao c = new Cartao(
                        rs.getString("num_cartao"),
                        rs.getString("nome_impresso"),
                        rs.getString("cod_seguranca"),
                        b,
                        null
                );
                c.setId(rs.getLong("id"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Bandeira> findAllBandeiras() {
        List<Bandeira> bandeiras = new ArrayList<>();
        String sql = "SELECT * FROM bandeira";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bandeira b = new Bandeira();
                b.setId(rs.getLong("id"));
                b.setDescricao(rs.getString("descricao"));
                bandeiras.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bandeiras;
    }

    public Bandeira findBandeiraById(Long id) {
        String sql = "SELECT * FROM bandeira WHERE id = ?";
        Bandeira bandeira = null;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                bandeira = new Bandeira();
                bandeira.setId(rs.getLong("id"));
                bandeira.setDescricao(rs.getString("descricao"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bandeira;
    }

    public void updatePorCliente(Long clienteId, List<Cartao> cartoes) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false);
            List<Long> idsExistentes = new ArrayList<>();
            String sqlBuscar = "SELECT id FROM cartao WHERE cliente_id = ?";
            try (PreparedStatement psBuscar = conn.prepareStatement(sqlBuscar)) {
                psBuscar.setLong(1, clienteId);
                ResultSet rs = psBuscar.executeQuery();
                while (rs.next()) {
                    idsExistentes.add(rs.getLong("id"));
                }
            }
            List<Long> idsRecebidos = new ArrayList<>();
            for (Cartao c : cartoes) {
                if (c.getId() != null) {
                    idsRecebidos.add(c.getId());
                    String sqlUpdate = "UPDATE cartao SET num_cartao = ?, nome_impresso = ?, cod_seguranca = ?, bandeira_id = ? WHERE id = ?";
                    try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                        psUpdate.setString(1, c.getNumCartao());
                        psUpdate.setString(2, c.getNomeImpresso());
                        psUpdate.setString(3, c.getCodSeguranca());
                        psUpdate.setObject(4, c.getBandeira() != null ? c.getBandeira().getId() : null, Types.BIGINT);
                        psUpdate.setLong(5, c.getId());
                        psUpdate.executeUpdate();
                    }
                } else {
                    String sqlInsert = "INSERT INTO cartao (num_cartao, nome_impresso, cod_seguranca, bandeira_id, cliente_id) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                        psInsert.setString(1, c.getNumCartao());
                        psInsert.setString(2, c.getNomeImpresso());
                        psInsert.setString(3, c.getCodSeguranca());
                        psInsert.setObject(4, c.getBandeira() != null ? c.getBandeira().getId() : null, Types.BIGINT);
                        psInsert.setLong(5, clienteId);
                        psInsert.executeUpdate();
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
