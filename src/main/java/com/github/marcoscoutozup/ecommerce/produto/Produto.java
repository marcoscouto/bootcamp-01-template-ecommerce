package com.github.marcoscoutozup.ecommerce.produto;

import com.github.marcoscoutozup.ecommerce.caracteristica.Caracteristica;
import com.github.marcoscoutozup.ecommerce.categoria.Categoria;
import com.github.marcoscoutozup.ecommerce.produto.adicionaropiniao.Opiniao;
import com.github.marcoscoutozup.ecommerce.produto.adicionarpergunta.Pergunta;
import com.github.marcoscoutozup.ecommerce.usuario.Usuario;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Produto {

    @Id
    @GeneratedValue(generator = "uuid4")
    private UUID id;

    @NotBlank
    private String nome;

    @NotNull
    private BigDecimal preco;

    @NotNull
    @PositiveOrZero
    private Integer quantidade;

    @ElementCollection //1
    private List<Caracteristica> caracteristicas;

    @NotBlank
    @Size(max = 1000)
    @Column(columnDefinition =  "text")
    private String descricao;

    @NotNull
    @ManyToOne //2
    private Categoria categoria;

    @NotNull
    @ManyToOne //3
    private Usuario usuario;

    @ElementCollection
    private List<String> imagens;

    @ElementCollection //4
    private List<Opiniao> opinioes;

    @ElementCollection //5
    private List<Pergunta> perguntas;

    @CreationTimestamp
    private LocalDateTime created_at;

    @Deprecated
    public Produto() {
    }

    public Produto(@NotBlank String nome, @NotNull BigDecimal preco, @NotNull @PositiveOrZero Integer quantidade, List<Caracteristica> caracteristicas, @NotBlank @Size(max = 1000) String descricao, @NotNull Categoria categoria, @NotNull Usuario usuario) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.caracteristicas = caracteristicas;
        this.descricao = descricao;
        this.categoria = categoria;
        this.usuario = usuario;
    }

    public boolean verificarSeEOProprietarioDoProduto(String email){
        return usuario.getEmail().equals(email);
    }

    public void adicionarListaDeImagensNoProduto(List<String> imagens){
        this.imagens.addAll(imagens);
    }

    public void adicionarOpiniaoAoProduto(Opiniao opiniao){
        this.opinioes.add(opiniao);
    }

    public void adicionarPerguntaAoProduto(Pergunta pergunta){
        this.perguntas.add(pergunta);
    }

    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

    public String prepararPerguntaDoProdutoParaEmail(){
        return "\n\n*** Olá, você tem uma nova pergunta sobre o produto "+ nome +" ***" +
                "\n\n" + perguntas.get(perguntas.size()-1).prepararPerguntaParaEmail() +
                "\n\nLink do Produto: " + "\n\n";
    }

    public String prepararDetalhesDoProdutoParaEmail(){
        return "\n\nProduto: " + nome +
                "\n\nPreço: " + preco +
                "\n\n";
    }

    public BigDecimal calcularMediaDeNotas(){                                       //6
        BigDecimal totalDasNotas = new BigDecimal(opinioes.stream().mapToDouble(Opiniao::getNota).sum());
        BigDecimal quantidadeDeNotas = new BigDecimal(getTotaldeNotas());
        return totalDasNotas.divide(quantidadeDeNotas).setScale(0, RoundingMode.CEILING);
    }

    public Integer getTotaldeNotas(){
       return opinioes.size();
    }

    public boolean verificarSeExisteEstoqueParaOperacao(Integer quantidade){
        return this.quantidade > 0 && this.quantidade >= quantidade;
    }

    public void abaterEstoque(Integer quantidade){
        this.quantidade = this.quantidade - quantidade;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public List<Caracteristica> getCaracteristicas() {
        return caracteristicas;
    }

    public String getDescricao() {
        return descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<String> getImagens() {
        return imagens;
    }

    public List<Opiniao> getOpinioes() {
        return opinioes;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                ", caracteristicas=" + caracteristicas +
                ", descricao='" + descricao + '\'' +
                ", categoria=" + categoria +
                ", usuario=" + usuario +
                ", imagens=" + imagens +
                ", opinioes=" + opinioes +
                ", perguntas=" + perguntas +
                '}';
    }
}
