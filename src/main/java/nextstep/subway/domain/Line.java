package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(final Station upStation, final Station downStation, final int distance) {
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(final String name, final String color) {
        if (name != null) {
            this.name = name;
        }

        if (color != null) {
            this.color = color;
        }
    }

    public boolean isLastDownStation(final Station station) {
        return getLastDownStation().equals(station);
    }

    private Station getLastDownStation() {
        return getLastSection().getDownStation();
    }

    private Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    public void removeLastSection() {
        sections.remove(sections.size() - 1);
    }

    public List<Station> getStations() {
        if (hasNoSection()) {
            return Collections.emptyList();
        }

        List<Station> stations = getAllDownStations();
        addFirstUpStation(stations);
        return stations;
    }

    private List<Station> getAllDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }


    private boolean hasNoSection() {
        return sections.isEmpty();
    }

    private void addFirstUpStation(final List<Station> stations) {
        stations.add(0, getFirstUpStation());
    }
    private Station getFirstUpStation() {
        return sections.get(0).getUpStation();
    }

}
