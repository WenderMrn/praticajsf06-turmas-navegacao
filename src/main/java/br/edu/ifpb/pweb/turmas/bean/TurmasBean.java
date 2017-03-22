package br.edu.ifpb.pweb.turmas.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;

import br.pweb.turmas.dao.AlunoDAO;
import br.pweb.turmas.dao.TurmaDAO;
import br.pweb.turmas.model.Aluno;
import br.pweb.turmas.model.Turma;

@ManagedBean
@ViewScoped
public class TurmasBean {
	private List<Turma> turmas;
	private long id;
	private Turma turma;
	private Aluno aluno;
	
	@PostConstruct
	public void init(){
		this.turma = new Turma();
		this.aluno = new Aluno();
		listarTurmas();
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
		TurmaDAO tDao = new TurmaDAO();
		Turma t = tDao.find(id);
		if(t != null)
			this.turma = t; 
	}
	
	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}
	
	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	
	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}
	
	public void listarTurmas() {
		TurmaDAO tDao = new TurmaDAO();
		this.turmas = tDao.findAll();
	}
	
	public void listarAlunosPorTurma(){
		TurmaDAO tDao = new TurmaDAO();
		Turma t = tDao.find(this.id);
		if(t != null){
			this.turma = t;
			this.turma.setAlunos(t.getAlunos());
		}
	}
	
	public void atualizarTurmas(ActionEvent e) {
		this.listarTurmas();
	}
	
	public void adicionar(){
		TurmaDAO tDao = new TurmaDAO();
		tDao.beginTransaction();
		tDao.insert(this.turma);
		tDao.commit();
		init();
	}
	
	public void adicionarAluno(){
		
		this.aluno.setTurma(this.turma);
		this.turma.getAlunos().add(this.aluno);
		
		AlunoDAO tDao = new AlunoDAO();
		tDao.beginTransaction();
		tDao.insert(this.aluno);
		tDao.commit();
		this.aluno = new Aluno();
	}
	
	public void excluirTurma(Turma t){
		TurmaDAO tDao = new TurmaDAO();
		tDao.beginTransaction();
		tDao.delete(t);
		tDao.commit();
		this.listarTurmas();
	}
	
	public void excluirAluno(Aluno a){
		
		TurmaDAO tDao = new TurmaDAO();
		tDao.beginTransaction();
		this.turma.getAlunos().remove(a);
		tDao.update(this.turma);
		tDao.commit();
		
		AlunoDAO aDao = new AlunoDAO();
		aDao.beginTransaction();
		aDao.delete(a);
		aDao.commit();
		this.listarAlunosPorTurma();
	}
	
	public void loadFlash(){
		Flash flash = FacesContext.getCurrentInstance()
				.getExternalContext().getFlash();
		flash.put("turma",this.turma);
		flash.put("turmas",this.turmas);
	}
	
	public void unloadFlash(){
		Flash flash = FacesContext.getCurrentInstance()
				.getExternalContext().getFlash();
		
		Turma t = (Turma)flash.get("turma");
		List<Turma> ts =(List<Turma>)flash.get("turmas");
		
		if(t != null)
			this.turma = t;
		if(ts != null)
			this.turmas = ts; 
	}
	
	public String selecionar(Turma t){
		this.turma = t;
		loadFlash();
		Random r = new Random();;
		String resultado =  "turma?faces-redirect=true";
		return (r.nextInt(2)+1)% 2 == 0?resultado:resultado+"&id="+t.getId();
	}
}
