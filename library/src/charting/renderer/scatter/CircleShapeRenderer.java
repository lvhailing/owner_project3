package charting.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import charting.buffer.ScatterBuffer;
import charting.interfaces.datasets.IScatterDataSet;
import charting.utils.ColorTemplate;
import charting.utils.Utils;
import charting.utils.ViewPortHandler;

/**
 * Created by wajdic on 15/06/2016.
 * Created at Time 09:08
 */
public class CircleShapeRenderer implements ShapeRenderer {

    @Override
    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler, ScatterBuffer buffer, Paint
            renderPaint, final float shapeSize) {

        final float shapeHalf = shapeSize / 2f;
        final float shapeHoleSizeHalf = Utils.convertDpToPixel(dataSet.getScatterShapeHoleRadius());
        final float shapeHoleSize = shapeHoleSizeHalf * 2.f;
        final float shapeStrokeSize = (shapeSize - shapeHoleSize) / 2.f;
        final float shapeStrokeSizeHalf = shapeStrokeSize / 2.f;

        final int shapeHoleColor = dataSet.getScatterShapeHoleColor();

        for (int i = 0; i < buffer.size(); i += 2) {

            if (!viewPortHandler.isInBoundsRight(buffer.buffer[i]))
                break;

            if (!viewPortHandler.isInBoundsLeft(buffer.buffer[i])
                    || !viewPortHandler.isInBoundsY(buffer.buffer[i + 1]))
                continue;

            renderPaint.setColor(dataSet.getColor(i / 2));

            if (shapeSize > 0.0) {
                renderPaint.setStyle(Paint.Style.STROKE);
                renderPaint.setStrokeWidth(shapeStrokeSize);

                c.drawCircle(
                        buffer.buffer[i],
                        buffer.buffer[i + 1],
                        shapeHoleSizeHalf + shapeStrokeSizeHalf,
                        renderPaint);

                if (shapeHoleColor != ColorTemplate.COLOR_NONE) {
                    renderPaint.setStyle(Paint.Style.FILL);

                    renderPaint.setColor(shapeHoleColor);
                    c.drawCircle(
                            buffer.buffer[i],
                            buffer.buffer[i + 1],
                            shapeHoleSizeHalf,
                            renderPaint);
                }
            } else {
                renderPaint.setStyle(Paint.Style.FILL);

                c.drawCircle(
                        buffer.buffer[i],
                        buffer.buffer[i + 1],
                        shapeHalf,
                        renderPaint);
            }
        }

    }

}
