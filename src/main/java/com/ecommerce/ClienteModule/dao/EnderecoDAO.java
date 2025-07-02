package com.ecommerce.ClienteModule.dao;

import com.ecommerce.ClienteModule.domain.*;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class EnderecoDAO implements IDAO<Endereco> {

    private final String url = "jdbc:h2:file:./data/meubanco";
    private final String user = "sa";
    private final String password = "";

    public EnderecoDAO() {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS endereco (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "tipo VARCHAR(20), " +
                    "logradouro VARCHAR(255), numero INT, bairro VARCHAR(100), cep VARCHAR(20), " +
                    "tipo_logradouro_id BIGINT, tipo_residencial_id BIGINT, cidade_id BIGINT, cliente_id BIGINT, " +
                    "FOREIGN KEY (tipo_logradouro_id) REFERENCES tipo_logradouro(id), " +
                    "FOREIGN KEY (tipo_residencial_id) REFERENCES tipo_residencial(id), " +
                    "FOREIGN KEY (cidade_id) REFERENCES cidade(id), " +
                    "FOREIGN KEY (cliente_id) REFERENCES cliente(id))"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TipoLogradouro> findAllLogradouro() {
        List<TipoLogradouro> tipos = new ArrayList<>();
        String sql = "SELECT id, descricao FROM tipo_logradouro";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TipoLogradouro tipo = new TipoLogradouro();
                tipo.setId(rs.getLong("id"));
                tipo.setDescricao(rs.getString("descricao"));
                tipos.add(tipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipos;
    }

    public TipoLogradouro findTipoLogradouroById(Long id) {
        String sql = "SELECT id, descricao FROM tipo_logradouro WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TipoLogradouro tipo = new TipoLogradouro();
                tipo.setId(rs.getLong("id"));
                tipo.setDescricao(rs.getString("descricao"));
                return tipo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Endereco endereco) {
        if (endereco == null || endereco.getCliente() == null) return;

        saveCidade(endereco.getCidade());

        String sql = "INSERT INTO endereco (tipo, logradouro, numero, cep, bairro, cidade_id, tipo_logradouro_id, tipo_residencial_id, cliente_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, endereco.getTipo());
            ps.setString(2, endereco.getLogradouro());
            ps.setInt(3, endereco.getNumero());
            ps.setString(4, endereco.getCep());
            ps.setString(5, endereco.getBairro());
            ps.setObject(6, endereco.getCidade() != null ? endereco.getCidade().getId() : null, Types.BIGINT);
            ps.setObject(7, endereco.getTipoLogradouro() != null ? endereco.getTipoLogradouro().getId() : null, Types.BIGINT);
            ps.setObject(8, endereco.getTipoResidencial() != null ? endereco.getTipoResidencial().getId() : null, Types.BIGINT);  // <-- Aqui
            ps.setLong(9, endereco.getCliente().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Endereco endereco) {

    }

    @Override
    public Endereco findById(Long id) {
        String sql = "SELECT * FROM endereco WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Endereco e = new Endereco();
                e.setId(rs.getLong("id"));
                e.setTipo(rs.getString("tipo"));
                e.setLogradouro(rs.getString("logradouro"));
                e.setNumero(rs.getInt("numero"));
                e.setCep(rs.getString("cep"));
                e.setBairro(rs.getString("bairro"));
                Long cidadeId = rs.getLong("cidade_id");
                if (!rs.wasNull()) {
                    e.setCidade(findCidadeById(cidadeId));
                }

                Long tipoLogradouroId = rs.getLong("tipo_logradouro_id");
                if (!rs.wasNull()) {
                    e.setTipoLogradouro(findTipoLogradouroById(tipoLogradouroId));
                }

                return e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Endereco> findAll() {
        return List.of();
    }

    public List<Endereco> findEnderecosByClienteId(Long clienteId) {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT * FROM endereco WHERE cliente_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, clienteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Endereco e = new Endereco();
                e.setId(rs.getLong("id"));
                e.setTipo(rs.getString("tipo"));
                e.setLogradouro(rs.getString("logradouro"));
                e.setNumero(rs.getInt("numero"));
                e.setBairro(rs.getString("bairro"));
                e.setCep(rs.getString("cep"));

                Long tipoLogradouroId = rs.getLong("tipo_logradouro_id");
                e.setTipoLogradouro(tipoLogradouroId != 0 ? findTipoLogradouroById(tipoLogradouroId) : null);

                Long tipoResidencialId = rs.getLong("tipo_residencial_id");
                e.setTipoResidencial(tipoResidencialId != 0 ? findTipoResidencialById(tipoResidencialId) : null);

                Long cidadeId = rs.getLong("cidade_id");
                e.setCidade(cidadeId != 0 ? findCidadeById(cidadeId) : null);

                enderecos.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enderecos;
    }


    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM endereco WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteByClienteId(Long clienteId) {
        String sql = "DELETE FROM endereco WHERE cliente_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, clienteId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public TipoResidencial findTipoResidencialById(Long id) {
        String sql = "SELECT id, descricao FROM tipo_residencial WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TipoResidencial tipo = new TipoResidencial();
                tipo.setId(rs.getLong("id"));
                tipo.setDescricao(rs.getString("descricao"));
                return tipo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cidade findCidadeById(Long id) {
        String sql = "SELECT id, descricao, estado_id FROM cidade WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cidade cidade = new Cidade();
                cidade.setId(rs.getLong("id"));
                cidade.setDescricao(rs.getString("descricao"));
                cidade.setEstado(findEstadoById(rs.getLong("estado_id")));
                return cidade;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Estado findEstadoById(Long id) {
        String sql = "SELECT id, descricao, uf FROM estado WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Estado estado = new Estado();
                estado.setId(rs.getLong("id"));
                estado.setDescricao(rs.getString("descricao"));
                estado.setUf(rs.getString("uf"));
                return estado;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveCidade(Cidade cidade) {
        if (cidade == null || cidade.getDescricao() == null || cidade.getEstado() == null) return;

        String checkSql = "SELECT id FROM cidade WHERE UPPER(descricao) = UPPER(?) AND estado_id = ?";
        String insertSql = "INSERT INTO cidade (descricao, estado_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, cidade.getDescricao());
            checkStmt.setLong(2, cidade.getEstado().getId());

            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) { // Não existe, insere
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    insertStmt.setString(1, cidade.getDescricao());
                    insertStmt.setLong(2, cidade.getEstado().getId());
                    insertStmt.executeUpdate();

                    ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        cidade.setId(generatedKeys.getLong(1));
                    }
                }
            } else {
                cidade.setId(rs.getLong("id")); // já existe, pega o ID
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Estado> findAllEstados() {
        List<Estado> estados = new ArrayList<>();
        String sql = "SELECT id, descricao, uf FROM estado";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Estado estado = new Estado();
                estado.setId(rs.getLong("id"));
                estado.setDescricao(rs.getString("descricao"));
                estado.setUf(rs.getString("uf"));
                estados.add(estado);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return estados;
    }

    public List<TipoResidencial> findAllResidencial() {
        List<TipoResidencial> tipos = new ArrayList<>();
        String sql = "SELECT id, descricao FROM tipo_residencial";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TipoResidencial tipo = new TipoResidencial();
                tipo.setId(rs.getLong("id"));
                tipo.setDescricao(rs.getString("descricao"));
                tipos.add(tipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipos;
    }

    public List<Endereco> findEnderecosCobrancaByClienteId(Long clienteId) {
        return findEnderecosByTipo(clienteId, "COBRANCA");
    }

    public List<Endereco> findEnderecosEntregaByClienteId(Long clienteId) {
        return findEnderecosByTipo(clienteId, "ENTREGA");
    }

    private List<Endereco> findEnderecosByTipo(Long clienteId, String tipo) {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT * FROM endereco WHERE cliente_id = ? AND tipo = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, clienteId);
            ps.setString(2, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Endereco e = new Endereco();
                e.setId(rs.getLong("id"));
                e.setLogradouro(rs.getString("logradouro"));
                e.setNumero(rs.getInt("numero"));
                e.setCep(rs.getString("cep"));
                e.setBairro(rs.getString("bairro"));
                e.setTipo(rs.getString("tipo"));
                Long tipoLogradouroId = rs.getLong("tipo_logradouro_id");
                e.setTipoLogradouro(tipoLogradouroId != 0 ? findTipoLogradouroById(tipoLogradouroId) : null);
                Long tipoResidencialId = rs.getLong("tipo_residencial_id");
                e.setTipoResidencial(tipoResidencialId != 0 ? findTipoResidencialById(tipoResidencialId) : null);
                Long cidadeId = rs.getLong("cidade_id");
                e.setCidade(cidadeId != 0 ? findCidadeById(cidadeId) : null);
                enderecos.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return enderecos;
    }


    public void updatePorCliente(Long clienteId, List<Endereco> enderecos) {
        if (enderecos == null) return;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false);

            List<Long> idsExistentes = new ArrayList<>();
            String sqlBuscar = "SELECT id FROM endereco WHERE cliente_id = ?";
            try (PreparedStatement psBuscar = conn.prepareStatement(sqlBuscar)) {
                psBuscar.setLong(1, clienteId);
                try (ResultSet rs = psBuscar.executeQuery()) {
                    while (rs.next()) {
                        idsExistentes.add(rs.getLong("id"));
                    }
                }
            }

            List<Long> idsRecebidos = new ArrayList<>();

            for (Endereco e : enderecos) {
                if (e.getId() != null && idsExistentes.contains(e.getId())) {
                    String sqlUpdate = "UPDATE endereco SET tipo = ?, logradouro = ?, numero = ?, bairro = ?, cep = ?, " +
                            "tipo_logradouro_id = ?, tipo_residencial_id = ?, cidade_id = ? WHERE id = ?";
                    try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                        psUpdate.setString(1, e.getTipo());
                        psUpdate.setString(2, e.getLogradouro());
                        psUpdate.setInt(3, e.getNumero());
                        psUpdate.setString(4, e.getBairro());
                        psUpdate.setString(5, e.getCep());
                        psUpdate.setObject(6, e.getTipoLogradouro() != null ? e.getTipoLogradouro().getId() : null, Types.BIGINT);
                        psUpdate.setObject(7, e.getTipoResidencial() != null ? e.getTipoResidencial().getId() : null, Types.BIGINT);
                        psUpdate.setObject(8, e.getCidade() != null ? e.getCidade().getId() : null, Types.BIGINT);
                        psUpdate.setLong(9, e.getId());
                        psUpdate.executeUpdate();
                    }
                    idsRecebidos.add(e.getId());
                } else {
                    saveCidade(e.getCidade());  // Garante que cidade está salva e com id
                    String sqlInsert = "INSERT INTO endereco (tipo, logradouro, numero, bairro, cep, tipo_logradouro_id, tipo_residencial_id, cidade_id, cliente_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                        psInsert.setString(1, e.getTipo());
                        psInsert.setString(2, e.getLogradouro());
                        psInsert.setInt(3, e.getNumero());
                        psInsert.setString(4, e.getBairro());
                        psInsert.setString(5, e.getCep());
                        psInsert.setObject(6, e.getTipoLogradouro() != null ? e.getTipoLogradouro().getId() : null, Types.BIGINT);
                        psInsert.setObject(7, e.getTipoResidencial() != null ? e.getTipoResidencial().getId() : null, Types.BIGINT);
                        psInsert.setObject(8, e.getCidade() != null ? e.getCidade().getId() : null, Types.BIGINT);
                        psInsert.setLong(9, clienteId);
                        psInsert.executeUpdate();
                        try (ResultSet generatedKeys = psInsert.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                e.setId(generatedKeys.getLong(1)); // atualiza o id do objeto
                            }
                        }
                    }
                    idsRecebidos.add(e.getId());
                }
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (!e.getSQLState().equals("08003")) {
                    DriverManager.getConnection(url, user, password).rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    public Cidade findCidadeByDescricaoAndEstado(String descricao, Long estadoId) {
        String sql = "SELECT id, descricao, estado_id FROM cidade WHERE UPPER(descricao) = UPPER(?) AND estado_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descricao);
            ps.setLong(2, estadoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cidade cidade = new Cidade();
                cidade.setId(rs.getLong("id"));
                cidade.setDescricao(rs.getString("descricao"));
                cidade.setEstado(findEstadoById(rs.getLong("estado_id")));
                return cidade;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
