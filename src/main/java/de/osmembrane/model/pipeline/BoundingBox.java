package de.osmembrane.model.pipeline;

import java.util.Objects;

public class BoundingBox {

    public static final BoundingBox WORLD = new BoundingBox(-90.0, -180.0, 90.0, +180.0);
    /**
     * North-western point of the bounding box
     */
    private final double nwLatitude, nwLongitude;

    /**
     * South-eastern point of the bounding box
     */
    private final double seLatitude, seLongitude;

    public BoundingBox(double nwLatitude,
            double nwLongitude,
            double seLatitude,
            double seLongitude) {
        if (isInvalidLatitude(nwLatitude) || isInvalidLatitude(seLatitude) || isInvalidLongitude(nwLongitude) || isInvalidLongitude(seLongitude))
            throw new IllegalStateException(
                    "Cannot create bounding box for illegal values: " + nwLatitude + ", " + nwLongitude + " -> " + seLatitude + ", " + seLongitude);

        this.nwLatitude = nwLatitude;
        this.nwLongitude = nwLongitude;
        this.seLatitude = seLatitude;
        this.seLongitude = seLongitude;
    }

    private boolean isInvalidLatitude(double latitude) {
        return !(latitude >= -90.0 && latitude <= 90.0);
    }

    private boolean isInvalidLongitude(double longitude) {
        return !(longitude >= -180.0 && longitude <= 180.0);
    }

    public double getNWLatitude() {
        return nwLatitude;
    }

    public double getNWLongitude() {
        return nwLongitude;
    }

    public double getSELatitude() {
        return seLatitude;
    }

    public double getSELongitude() {
        return seLongitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BoundingBox that = (BoundingBox) o;
        return Double.compare(that.nwLatitude, nwLatitude) == 0
               && Double.compare(that.nwLongitude, nwLongitude) == 0
               && Double.compare(that.seLatitude, seLatitude) == 0
               && Double.compare(that.seLongitude, seLongitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nwLatitude, nwLongitude, seLatitude, seLongitude);
    }
}
