package pl.allegro.jdd;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.TreeTraverser;

import java.util.function.Function;

public class StructureDiff {
    public Changes calculate(Employee oldCTO, Employee newCTO) {
        checkNotNull(oldCTO);
        checkNotNull(newCTO);

        MapDifference<Employee, Integer> difference = Maps.difference(
            getSalaryMap(oldCTO),
            getSalaryMap(newCTO)
        );

        return new Changes(
            difference.entriesOnlyOnLeft().keySet(),
            difference.entriesOnlyOnRight().keySet(),
            difference.entriesDiffering().keySet()
        );
    }

    private ImmutableMap<Employee, Integer> getSalaryMap(Employee cto) {
        return treeTraverserOf(Employee::getSubordinates)
            .breadthFirstTraversal(cto)
            .toMap(Employee::getSalary);
    }

    private static <T> TreeTraverser<T> treeTraverserOf(Function<T, Iterable<T>> childrenFunction) {
        return new TreeTraverser<T>() {
            @Override
            public Iterable<T> children(T root) {
                return childrenFunction.apply(root);
            }
        };
    }
}
