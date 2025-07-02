package com.ecommerce.ClienteModule.dao;

import com.ecommerce.ClienteModule.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ClienteDAO implements IDAO<Cliente> {

    private final String url = "jdbc:h2:file:./data/meubanco";
    private final String user = "sa";
    private final String password = "";

    @Autowired
    private CartaoDAO cartaoDAO;

    @Autowired
    private EnderecoDAO enderecoDAO;

    public ClienteDAO() {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS genero (id BIGINT AUTO_INCREMENT PRIMARY KEY, descricao VARCHAR(255) UNIQUE)");
            stmt.execute("CREATE TABLE IF NOT EXISTS cliente (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(255), dt_nasc VARCHAR(20), " +
                    "cpf VARCHAR(20), email VARCHAR(255), senha VARCHAR(255), status VARCHAR(20), " +
                    "ranking INT, codigo VARCHAR(50) UNIQUE, genero_id BIGINT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS telefone (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, tipo_telefone_id BIGINT, ddd_id BIGINT, " +
                    "numero VARCHAR(20), cliente_id BIGINT UNIQUE, FOREIGN KEY (cliente_id) REFERENCES cliente(id))");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Cliente cliente) {

        Long generoId = (cliente.getGenero() != null) ? cliente.getGenero().getId() : null;

        Long clienteId = null;
        String sql = "INSERT INTO cliente (nome, dt_nasc, cpf, email, senha, status, ranking, codigo, genero_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getDtNasc());
            ps.setString(3, cliente.getCpf());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getSenha());
            ps.setString(6, cliente.getStatus());
            ps.setObject(7, cliente.getRanking(), Types.INTEGER);
            ps.setString(8, cliente.getCodigo());
            ps.setObject(9, generoId, Types.BIGINT);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) clienteId = rs.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (clienteId != null) {
            cliente.setId(clienteId);
            saveTelefone(cliente.getTelefone(), clienteId);
        }
    }

    @Override
    public Cliente findById(Long id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cliente c = montarCliente(rs);
                c.setCartoes(cartaoDAO.buscarCartoesPorClienteId(id));
                c.setTelefone(buscarTelefonePorClienteId(id));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Cliente> findAll() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = montarCliente(rs);
                Long id = c.getId();
                c.setCartoes(cartaoDAO.buscarCartoesPorClienteId(id));
                //c.setTelefone(buscarTelefonePorClienteId(id));
                clientes.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    @Override
    public void delete(Long id) {
        cartaoDAO.deleteByClienteId(id);
        deleteTelefonePorClienteId(id);
        enderecoDAO.deleteByClienteId(id);
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM cliente WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, dt_nasc = ?, cpf = ?, email = ?, senha = ?, status = ?, ranking = ?, codigo = ?, genero_id = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getDtNasc());
            ps.setString(3, cliente.getCpf());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getSenha());
            ps.setString(6, cliente.getStatus());
            ps.setObject(7, cliente.getRanking(), Types.INTEGER);
            ps.setString(8, cliente.getCodigo());
            ps.setObject(9, cliente.getGenero() != null ? cliente.getGenero().getId() : null, Types.BIGINT);
            ps.setLong(10, cliente.getId());

            ps.executeUpdate();

            updateTelefone(cliente.getTelefone(), cliente.getId());

            if (cliente.getCartoes() != null) {
                cartaoDAO.updatePorCliente(cliente.getId(), cliente.getCartoes());
            }

            if (cliente.getEnderecosCobranca() != null) {
                enderecoDAO.updatePorCliente(cliente.getId(), cliente.getEnderecosCobranca());
            }
            if (cliente.getEnderecosEntrega() != null) {
                enderecoDAO.updatePorCliente(cliente.getId(), cliente.getEnderecosEntrega());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveTelefone(Telefone telefone, Long clienteId) {
        if (telefone == null) return;
        String sql = "INSERT INTO telefone (tipo_telefone_id, ddd_id, numero, cliente_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, telefone.getTipoTelefone() != null ? telefone.getTipoTelefone().getId() : null, Types.BIGINT);
            ps.setObject(2, telefone.getDdd() != null ? telefone.getDdd().getId() : null, Types.BIGINT);
            ps.setString(3, telefone.getNumero());
            ps.setLong(4, clienteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Telefone buscarTelefonePorClienteId(Long clienteId) {
        Telefone t = null;
        String sql = "SELECT t.id, t.numero, tt.id AS tipo_telefone_id, tt.descricao AS tipo_telefone_desc, " +
                "d.id AS ddd_id, d.numero AS ddd_numero " +
                "FROM telefone t " +
                "LEFT JOIN tipo_telefone tt ON t.tipo_telefone_id = tt.id " +
                "LEFT JOIN ddd d ON t.ddd_id = d.id " +
                "WHERE t.cliente_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, clienteId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                t = new Telefone();
                t.setId(rs.getLong("id"));
                t.setNumero(rs.getString("numero"));

                TipoTelefone tipoTelefone = null;
                if (rs.getObject("tipo_telefone_id") != null) {
                    tipoTelefone = new TipoTelefone();
                    tipoTelefone.setId(rs.getLong("tipo_telefone_id"));
                    tipoTelefone.setDescricao(rs.getString("tipo_telefone_desc"));
                }
                t.setTipoTelefone(tipoTelefone);

                DDD ddd = null;
                if (rs.getObject("ddd_id") != null) {
                    ddd = new DDD();
                    ddd.setId(rs.getLong("ddd_id"));
                    ddd.setNumero(rs.getString("ddd_numero"));
                }
                t.setDdd(ddd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }


    private void deleteTelefonePorClienteId(Long clienteId) {
        String sql = "DELETE FROM telefone WHERE cliente_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, clienteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Cliente montarCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id"));
        c.setNome(rs.getString("nome"));
        c.setDtNasc(rs.getString("dt_nasc"));
        c.setCpf(rs.getString("cpf"));
        c.setEmail(rs.getString("email"));
        c.setSenha(rs.getString("senha"));
        c.setStatus(rs.getString("status"));
        c.setRanking(rs.getInt("ranking"));
        c.setCodigo(rs.getString("codigo"));
        c.setGenero(new Genero(rs.getLong("genero_id")));
        return c;
    }

    public List<Genero> findAllGeneros() {
        List<Genero> generos = new ArrayList<>();
        String sql = "SELECT id, descricao FROM genero";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Genero genero = new Genero();
                genero.setId(rs.getLong("id"));
                genero.setDescricao(rs.getString("descricao"));
                generos.add(genero);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generos;
    }

    public List<TipoTelefone> findAllTelefone() {
        List<TipoTelefone> tipos = new ArrayList<>();
        String sql = "SELECT id, descricao FROM tipo_telefone";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TipoTelefone tipo = new TipoTelefone();
                tipo.setId(rs.getLong("id"));
                tipo.setDescricao(rs.getString("descricao"));
                tipos.add(tipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipos;
    }

    public List<DDD> findAllDDD() {
        List<DDD> ddds = new ArrayList<>();
        String sql = "SELECT id, numero FROM ddd";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DDD ddd = new DDD();
                ddd.setId(rs.getLong("id"));
                ddd.setNumero(rs.getString("numero"));
                ddds.add(ddd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ddds;
    }

    public void atualizarCartaoPreferencial(Long clienteId, Long cartaoPreferencialId) {
        String sql = "UPDATE cliente SET id_cartao_preferencial = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartaoPreferencialId);
            ps.setLong(2, clienteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTelefone(Telefone telefone, Long clienteId) {
        if (telefone == null) return;

        String sql = "UPDATE telefone SET tipo_telefone_id = ?, ddd_id = ?, numero = ? WHERE cliente_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, telefone.getTipoTelefone() != null ? telefone.getTipoTelefone().getId() : null, Types.BIGINT);
            ps.setObject(2, telefone.getDdd() != null ? telefone.getDdd().getId() : null, Types.BIGINT);
            ps.setString(3, telefone.getNumero());
            ps.setLong(4, clienteId);
            int rows = ps.executeUpdate();

            // se não existe, faz insert
            if (rows == 0) saveTelefone(telefone, clienteId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
