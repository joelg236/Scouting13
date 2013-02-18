package edu.ata.scouting;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Factory for create {@link GridBagConstraints}.
 *
 * @author Joel Gallant
 */
public class LayoutFactory extends GridBagConstraints {

    /**
     * Creates a {@link GridBagLayout} for convenience.
     *
     * @return new grid bag layout
     */
    public static GridBagLayout createLayout() {
        return new GridBagLayout();
    }

    /**
     * Creates new factory with defaults.
     *
     * @return new factory
     */
    public static LayoutFactory newFactory() {
        return new LayoutFactory().setAnchor(FIRST_LINE_START).setFill(BOTH);
    }

    /**
     * Creates new factory with given arguments as starting values.
     *
     * @param anchor anchor of grid
     * @return the factory
     */
    public static LayoutFactory newFactory(int anchor) {
        LayoutFactory l = new LayoutFactory();
        l.anchor = anchor;
        return l;
    }

    /**
     * Creates new factory with given arguments as starting values.
     *
     * @param anchor anchor of grid
     * @param fill fill area
     * @return the factory
     */
    public static LayoutFactory newFactory(int anchor, int fill) {
        LayoutFactory l = new LayoutFactory();
        l.anchor = anchor;
        l.fill = fill;
        return l;
    }

    /**
     * Creates new factory with given arguments as starting values.
     *
     * @param anchor anchor of grid
     * @param fill fill area
     * @param insets insets on sides
     * @return the factory
     */
    public static LayoutFactory newFactory(int anchor, int fill, Insets insets) {
        LayoutFactory l = new LayoutFactory();
        l.anchor = anchor;
        l.fill = fill;
        l.insets = insets;
        return l;
    }

    /**
     * Creates new factory with given arguments as starting values.
     *
     * @param fill fill area
     * @param weightx weight of width
     * @param weighty weight of height
     * @return the factory
     */
    public static LayoutFactory newFactory(int fill, int weightx, int weighty) {
        LayoutFactory l = new LayoutFactory();
        l.fill = fill;
        l.weightx = weightx;
        l.weighty = weighty;
        return l;
    }

    /**
     * Creates new factory with given arguments as starting values.
     *
     * @param gridx grid x position
     * @param gridy grid y position
     * @param gridwidth height of element (on grid)
     * @param gridheight width of element (on grid)
     * @param weightx weight of width
     * @param weighty weight of height
     * @param anchor anchor of grid
     * @param fill fill area
     * @param insets insets on sides
     * @return the factory
     */
    public static LayoutFactory newFactory(int gridx, int gridy, int gridwidth,
            int gridheight, double weightx, double weighty, int anchor, int fill,
            Insets insets) {
        LayoutFactory l = new LayoutFactory();
        l.anchor = anchor;
        l.fill = fill;
        l.gridheight = gridheight;
        l.gridwidth = gridwidth;
        l.gridx = gridx;
        l.gridy = gridy;
        l.insets = insets;
        l.weightx = weightx;
        l.weighty = weighty;
        return l;
    }

    /**
     * Sets the grid x position.
     *
     * @param x grid x position
     * @return the factory
     */
    public LayoutFactory setX(int x) {
        this.gridx = x;
        return this;
    }

    /**
     * Sets the grid y position.
     *
     * @param y grid y position
     * @return the factory
     */
    public LayoutFactory setY(int y) {
        this.gridy = y;
        return this;
    }

    /**
     * Sets the weight of the width
     *
     * @param x width weight
     * @return the factory
     */
    public LayoutFactory setWeightX(double x) {
        this.weightx = x;
        return this;
    }

    /**
     * Sets the weight of the height
     *
     * @param y height weight
     * @return the factory
     */
    public LayoutFactory setWeightY(double y) {
        this.weighty = y;
        return this;
    }

    /**
     * Sets the width on the grid
     *
     * @param width width on grid
     * @return the factory
     */
    public LayoutFactory setWidth(int width) {
        this.gridwidth = width;
        return this;
    }

    /**
     * Sets the height on the grid
     *
     * @param height height on grid
     * @return the factory
     */
    public LayoutFactory setHeight(int height) {
        this.gridheight = height;
        return this;
    }

    /**
     * Sets the anchor of grid
     *
     * @param anchor anchor of grid
     * @return the factory
     */
    public LayoutFactory setAnchor(int anchor) {
        this.anchor = anchor;
        return this;
    }

    /**
     * Sets the fill range of element
     *
     * @param fill fill area
     * @return the factory
     */
    public LayoutFactory setFill(int fill) {
        this.fill = fill;
        return this;
    }

    /**
     * Sets the insets of element
     *
     * @param insets insets on sides
     * @return the factory
     */
    public LayoutFactory setInsets(Insets insets) {
        this.insets = insets;
        return this;
    }
}