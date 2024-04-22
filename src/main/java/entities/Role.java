package entities;

import java.util.ArrayList;
import java.util.List;

public class Role {
    private int id;
    private String nom_role;
    private List<Utilisateur> utilisateurs = new ArrayList<>();

    public Role() {
    }

    public Role(int id, String nom_role) {
        this.id = id;
        this.nom_role = nom_role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_role() {
        return nom_role;
    }

    public void setNom_role(String nom_role) {
        this.nom_role = nom_role;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", nom_role='" + nom_role + '\'' +
                '}';
    }

    // Constructeur, getters et setters pour les autres champs
    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public void addUtilisateur(Utilisateur utilisateur) {
        utilisateurs.add(utilisateur);
        utilisateur.setRole_id(this.getId());
    }

    public void removeUtilisateur(Utilisateur utilisateur) {
        utilisateurs.remove(utilisateur);
        utilisateur.setRole_id(0);
    }
}
