package de.osmembrane.controller.mapper;

import de.osmembrane.model.pipeline.BoundingBox;
import de.osmembrane.resources.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringBoundingBoxMapper {

    public BoundingBox toBoundingBox(String string) throws IllegalArgumentException {
        String[] parts = string.split(Constants.BBOX_SEPERATOR);
        if (parts.length != 4)
            throw new IllegalArgumentException("String has " + parts.length + " instead of exactly 4 parts");

        List<Double> doubles = Arrays.stream(parts).map(it -> {
            try {
                return Double.valueOf(it);
            } catch (Exception e) {
                return null;
            }
        }).collect(Collectors.toList());

        int firstNullIndex = doubles.indexOf(null);
        if (firstNullIndex != -1)
            throw new IllegalArgumentException("At least part " + firstNullIndex + " was not a valid double: " + parts[firstNullIndex]);

        return new BoundingBox(doubles.get(0), doubles.get(1), doubles.get(2), doubles.get(3));
    }

    public String toString(BoundingBox boundingBox) {
        return String.format("%.7f%s%.7f%s%.7f%s%.7f",
                boundingBox.getNWLatitude(), Constants.BBOX_SEPERATOR,
                boundingBox.getNWLongitude(), Constants.BBOX_SEPERATOR,
                boundingBox.getSELatitude(), Constants.BBOX_SEPERATOR,
                boundingBox.getSELongitude());
    }

}
