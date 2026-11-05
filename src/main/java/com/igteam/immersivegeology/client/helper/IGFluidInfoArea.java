package com.igteam.immersivegeology.client.helper;

import blusunrize.immersiveengineering.client.gui.info.FluidInfoArea;
import blusunrize.immersiveengineering.client.gui.info.InfoArea;
import blusunrize.immersiveengineering.client.utils.GuiHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.IFluidTank;

import java.util.List;

/** Fluid gauge using the original GUI texture's overlay region. */
public class IGFluidInfoArea extends InfoArea
{
    private final IFluidTank tank;
    private final Rect2i area;
    private final int u, v, width, height;
    private final ResourceLocation texture;

    public IGFluidInfoArea(IFluidTank tank, Rect2i area, int u, int v, int width, int height, ResourceLocation texture)
    {
        super(area);
        this.tank = tank;
        this.area = area;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.texture = texture;
    }

    @Override
    public void fillTooltipOverArea(int mouseX, int mouseY, List<Component> tooltip)
    {
        FluidInfoArea.fillTooltip(tank.getFluid(), tank.getCapacity(), tooltip::add);
    }

    @Override
    public void draw(GuiGraphics graphics)
    {
        if(!tank.getFluid().isEmpty() && tank.getCapacity() > 0)
        {
            int fluidHeight = (int)(area.getHeight()*(tank.getFluidAmount()/(float)tank.getCapacity()));
            GuiHelper.drawRepeatedFluidSpriteGui(graphics.bufferSource(), graphics.pose(), tank.getFluid(),
                    area.getX(), area.getY()+area.getHeight()-fluidHeight, area.getWidth(), fluidHeight);
        }
        if(width > 0 && height > 0)
            graphics.blit(texture, area.getX()+(area.getWidth()-width)/2,
                    area.getY()+(area.getHeight()-height)/2, u, v, width, height);
    }
}
