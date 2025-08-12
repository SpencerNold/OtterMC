package io.github.ottermc.stats;

public class Statistic<T extends Number> {

    private double mean, m2;
    private long n;

    public Statistic() {
        this.mean = m2 = 0;
        this.n = 0;
    }

    public void add(T number) {
        n++;
        double num = number.doubleValue();
        double delta = num - mean;
        mean += delta / n;
        double delta2 = num - mean;
        m2 += delta * delta2;
    }

    public double getMean() {
        return mean;
    }

    public double getPopulationVariance() {
        return m2 / ((double) n);
    }

    public double getPopulationStandardDeviation() {
        return Math.sqrt(getPopulationVariance());
    }

    public double getSampleVariance() {
        return m2 / ((double) n - 1);
    }

    public double getSampleStandardDeviation() {
        return Math.sqrt(getSampleVariance());
    }
}
