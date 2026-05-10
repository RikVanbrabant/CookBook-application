package be.ucll.fs.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table(name = "family")
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Family name is required")
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "head_of_family_id", nullable = false)
    @JsonIgnore
    private User headOfFamily;

    @ManyToMany
    @JoinTable(
            name = "family_members",
            joinColumns = @JoinColumn(name = "family_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> members = new HashSet<>();

    @OneToMany(mappedBy = "family")
    @JsonIgnore
    private Set<Cookbook> familyCookbooks = new HashSet<>();

    protected Family() {}

    public Family(String name, User headOfFamily) {
        this.name = name;
        this.headOfFamily = headOfFamily;
        this.members.add(headOfFamily); // head is always a member
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getHeadOfFamily() {
        return headOfFamily;
    }

    public void setHeadOfFamily(User headOfFamily) {
        this.headOfFamily = headOfFamily;
    }

    public Set<User> getMembers() {
        return members;
    }

    public Set<Cookbook> getFamilyCookbooks() {
        return familyCookbooks;
    }

    public void addMember(User user) {
        this.members.add(user);
    }

    public void removeMember(User user) {
        this.members.remove(user);
    }

    public boolean hasMember(User user) {
        if (user == null) return false;
        return members.stream().anyMatch(m -> m.getId().equals(user.getId()));
    }

    public boolean isHeadOfFamily(User user) {
        if (user == null || headOfFamily == null) return false;
        return headOfFamily.getId().equals(user.getId());
    }

    @JsonProperty("headOfFamily")
    public String getHeadOfFamilyUsername() {
        return headOfFamily != null ? headOfFamily.getUsername() : null;
    }

    @JsonProperty("members")
    public Set<String> getMemberUsernames() {
        return members.stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());
    }
}
