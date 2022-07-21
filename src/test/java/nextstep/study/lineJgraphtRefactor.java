package nextstep.study;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.utils.LineTestSources.section;
import static nextstep.subway.utils.StationTestSources.station;
import static org.assertj.core.api.Assertions.assertThat;

class lineJgraphtRefactor {

    @Test
    void 최단거리조회_1구간() {
        final Line line = new Line();
        final Station station1 = station(1);
        final Station station2 = station(2);

        line.addSection(section(station1, station2));

        final LineGraph lineGraph = new LineGraph(line);
        lineGraph.addStationToVertex();
        lineGraph.addSectionToEdge();

        List<Station> result = lineGraph.findShortestPath(station1, station2);
        assertThat(result).hasSize(2);
        result = lineGraph.findShortestPath(station1, station2);
        assertThat(result).hasSize(2);
        result = lineGraph.findShortestPath(station1, station2);
        assertThat(result).hasSize(2);
        result = lineGraph.findShortestPath(station1, station2);
        assertThat(result).hasSize(2);
        result = lineGraph.findShortestPath(station1, station2);
        assertThat(result).hasSize(2);
        result = lineGraph.findShortestPath(station1, station2);
        assertThat(result).hasSize(2);
        result = lineGraph.findShortestPath(station1, station2);
        assertThat(result).hasSize(2);
    }

    @Test
    void 최단거리조회_2구간() {
        final Line line = new Line();
        final Station station1 = station(1);
        final Station station2 = station(2);
        final Station station3 = station(3);

        line.addSection(section(station2, station3));
        line.addSection(section(station1, station2));

        final LineGraph lineGraph = new LineGraph(line);

        List<Station> result = lineGraph.findShortestPath(station1, station3);
        assertThat(result).hasSize(3);
    }

    @Test
    void 최단거리조회_3구간() {
        final Line line = new Line();
        final Station station1 = station(1);
        final Station station2 = station(2);
        final Station station3 = station(3);
        final Station station4 = station(4);

        // when (4, 3), (3, 1), (1, 2) -> (1, 2), (3, 1), (4, 3)
        line.addSection(section(station1, station2));
        line.addSection(section(station3, station1));
        line.addSection(section(station4, station3));

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(station1);
        graph.addVertex(station2);
        graph.addVertex(station3);
        graph.addVertex(station4);

        graph.setEdgeWeight(graph.addEdge(station1, station2), 10);
        graph.setEdgeWeight(graph.addEdge(station3, station1), 10);
        graph.setEdgeWeight(graph.addEdge(station4, station3), 10);

        final LineGraph lineGraph = new LineGraph(line);
        List<Station> result = lineGraph.findShortestPath(station2, station4);

        assertThat(result).hasSize(4);
    }

    static class LineGraph {

        private final Line line;
        private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

        public LineGraph(final Line line) {
            this.line = line;
            this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
            init();
        }

        private void init() {
            addStationToVertex();
            addSectionToEdge();
        }

        private void addStationToVertex() {
            line.getStations().forEach(graph::addVertex);
        }

        private void addSectionToEdge() {
            line.getSections().forEach(v -> graph.setEdgeWeight(graph.addEdge(v.getUpStation(), v.getDownStation()), v.getDistance()));
        }

        public List<Station> findShortestPath(final Station from, final Station to) {
            final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            return dijkstraShortestPath.getPath(from, to).getVertexList();
        }
    }
}
