package nextstep.subway.domain;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private final Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void update(final String name, final String color) {
        if (name != null) {
            this.name = name;
        }

        if (color != null) {
            this.color = color;
        }
    }

    public void removeLastSection() {
        sections.removeLast();
    }

    public boolean hasNoSection() {
        return sections.isEmpty();
    }

    public List<Station> getStations() {
        if (hasNoSection()) {
            return Collections.emptyList();
        }

        return sections.findAllStationsInOrder();
    }

    public void addSection(final Section section) {
        sections.add(section);
        section.setLine(this);
    }

    public void addSection(final int index, final Section section) {
        sections.add(index, section);
        section.setLine(this);
    }

    public void addSection(final Section target, final Section section) {
        sections.add(getSections().indexOf(target) + 1, section);
        section.setLine(this);
    }

    public boolean isLastDownStation(final Station station) {
        final List<Station> stations = getStations();

        return stations.get(stations.size() - 1).equals(station);
    }

    public boolean isFirstStation(final Station station) {
        final List<Station> stations = getStations();
        return stations.get(0).equals(station);
    }

    public boolean containsStation(final Station station) {
        return getStations().contains(station);
    }
}
