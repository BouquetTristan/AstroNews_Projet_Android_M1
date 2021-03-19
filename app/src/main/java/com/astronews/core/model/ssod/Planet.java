package com.astronews.core.model.ssod;

import java.util.ArrayList;
import java.util.Locale;

public class Planet {
    private String id;
    private String name;
    private String englishName;
    private Boolean isPlanet;
    private ArrayList<PlanetRef> moons;
    private float semimajorAxis;
    private float perihelion;
    private float aphelion;
    private float eccentricity;
    private float inclination;
    private PlanetSizedNumber mass;
    private PlanetSizedNumber vol;
    private float density;
    private float gravity;
    private float escape;
    private float meanRadius;
    private float equaRadius;
    private float polarRadius;
    private float flattening;
    private String dimension;
    private float sideralOrbit;
    private float sideralRotation;
    private PlanetRef aroundPlanet;
    private String discoveredBy;
    private String discoveryDate;
    private String alternativeName;
    private float axialTilt;

    public Planet() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public Boolean getPlanet() {
        return isPlanet;
    }

    public void setPlanet(Boolean planet) {
        isPlanet = planet;
    }

    public ArrayList<PlanetRef> getMoons() {
        return moons;
    }

    public void setMoons(ArrayList<PlanetRef> moons) {
        this.moons = moons;
    }

    public float getSemimajorAxis() {
        return semimajorAxis;
    }

    public void setSemimajorAxis(float semimajorAxis) {
        this.semimajorAxis = semimajorAxis;
    }

    public float getPerihelion() {
        return perihelion;
    }

    public void setPerihelion(float perihelion) {
        this.perihelion = perihelion;
    }

    public float getAphelion() {
        return aphelion;
    }

    public void setAphelion(float aphelion) {
        this.aphelion = aphelion;
    }

    public float getEccentricity() {
        return eccentricity;
    }

    public void setEccentricity(float eccentricity) {
        this.eccentricity = eccentricity;
    }

    public float getInclination() {
        return inclination;
    }

    public void setInclination(float inclination) {
        this.inclination = inclination;
    }

    public PlanetSizedNumber getMass() {
        return mass;
    }

    public void setMass(PlanetSizedNumber mass) {
        this.mass = mass;
    }

    public PlanetSizedNumber getVol() {
        return vol;
    }

    public void setVol(PlanetSizedNumber vol) {
        this.vol = vol;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public float getEscape() {
        return escape;
    }

    public void setEscape(float escape) {
        this.escape = escape;
    }

    public float getMeanRadius() {
        return meanRadius;
    }

    public void setMeanRadius(float meanRadius) {
        this.meanRadius = meanRadius;
    }

    public float getEquaRadius() {
        return equaRadius;
    }

    public void setEquaRadius(float equaRadius) {
        this.equaRadius = equaRadius;
    }

    public float getPolarRadius() {
        return polarRadius;
    }

    public void setPolarRadius(float polarRadius) {
        this.polarRadius = polarRadius;
    }

    public float getFlattening() {
        return flattening;
    }

    public void setFlattening(float flattening) {
        this.flattening = flattening;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public float getSideralOrbit() {
        return sideralOrbit;
    }

    public void setSideralOrbit(float sideralOrbit) {
        this.sideralOrbit = sideralOrbit;
    }

    public float getSideralRotation() {
        return sideralRotation;
    }

    public void setSideralRotation(float sideralRotation) {
        this.sideralRotation = sideralRotation;
    }

    public PlanetRef getAroundPlanet() {
        return aroundPlanet;
    }

    public void setAroundPlanet(PlanetRef aroundPlanet) {
        this.aroundPlanet = aroundPlanet;
    }

    public String getDiscoveredBy() {
        return discoveredBy;
    }

    public void setDiscoveredBy(String discoveredBy) {
        this.discoveredBy = discoveredBy;
    }

    public String getDiscoveryDate() {
        return discoveryDate;
    }

    public void setDiscoveryDate(String discoveryDate) {
        this.discoveryDate = discoveryDate;
    }

    public String getAlternativeName() {
        return alternativeName;
    }

    public void setAlternativeName(String alternativeName) {
        this.alternativeName = alternativeName;
    }

    public float getAxialTilt() {
        return axialTilt;
    }

    public void setAxialTilt(float axialTilt) {
        this.axialTilt = axialTilt;
    }

    @Override
    public String toString() {
        return String.format(Locale.FRANCE, "nom: " + name + ", id: " + id);
    }
}
