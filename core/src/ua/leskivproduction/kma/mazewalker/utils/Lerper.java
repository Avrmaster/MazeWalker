package ua.leskivproduction.kma.mazewalker.utils;

import com.badlogic.gdx.graphics.Color;

public final class Lerper {
    private Lerper() {

    }

    public static float lerp(float start, float end, float progress) {
        if (progress >= 1)
            return end;
        if (progress <= 0)
            return start;

        return start*(1-progress) + (progress)*end;
    }

    public static void lerp(Color start, Color goal, float progress) {
        lerp(start, start, goal, progress);
    }

    public static void lerp(Color dest, Color start, Color goal, float progress) {
        dest.r = lerp(start.r, goal.r, progress);
        dest.g = lerp(start.g, goal.g, progress);
        dest.b = lerp(start.b, goal.b, progress);
        dest.a = lerp(start.a, goal.a, 5*progress);
    }

}
