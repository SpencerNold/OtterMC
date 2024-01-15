package io.github.ottermc.screen.hud.minecraft;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import io.github.ottermc.screen.hud.Component;
import io.github.ottermc.screen.hud.Movable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class MinecraftScoreboardHud extends Component implements Movable {
	
	private int xOffs, yOffs;
	
	public MinecraftScoreboardHud() {
		super(true);
	}
	
	@Override
	public void draw(Minecraft mc, GuiIngame gui, ScaledResolution res, float partialTicks) {
		Entity player = mc.thePlayer;
		World world = mc.theWorld;
		net.minecraft.scoreboard.Scoreboard scoreboard = world.getScoreboard();
		ScoreObjective objective = null;
		ScorePlayerTeam team = scoreboard.getPlayersTeam(((EntityPlayer) player).getName());
		if (team != null) {
			int index = team.getChatFormat().getColorIndex();
			if (index >= 0)
				objective = scoreboard.getObjectiveInDisplaySlot(3 + index);
		}
		objective = objective != null ? objective : scoreboard.getObjectiveInDisplaySlot(1);
		if (objective != null)
			renderScoreboard(mc, objective, res);
	}

	private void renderScoreboard(Minecraft mc, ScoreObjective objective, ScaledResolution res) {
		net.minecraft.scoreboard.Scoreboard scoreboard = objective.getScoreboard();
		Collection<Score> scores = scoreboard.getSortedScores(objective);
		List<Score> copy = Lists.newArrayList(Iterables.filter(scores, score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#")));
		if (copy.size() > 15)
			scores = Lists.newArrayList(Iterables.skip(copy, scores.size() - 15));
		else
			scores = copy;
		int width = mc.fontRendererObj.getStringWidth(objective.getDisplayName());
		for (Score score : scores) {
			ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
			String name = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
			width = Math.max(width, mc.fontRendererObj.getStringWidth(name));
		}
		int height = scores.size() * mc.fontRendererObj.FONT_HEIGHT;
		this.width = width;
		this.height = height + mc.fontRendererObj.FONT_HEIGHT + 1;
		int y = res.getScaledHeight() / 2 + height / 3;
		int x = res.getScaledWidth() - width - 3;
		this.x = x - 2;
		this.y = y - 1 - height - mc.fontRendererObj.FONT_HEIGHT;
		int index = 0;
		for (Score score : scores) {
			++index;
			ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score.getPlayerName());
			String name = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score.getPlayerName());
			String points = EnumChatFormatting.RED + "" + score.getScorePoints();
			int y2 = y - index * mc.fontRendererObj.FONT_HEIGHT;
			int x2 = res.getScaledWidth() - 5;
			Gui.drawRect(x - 2 + xOffs, y2 + yOffs, x2 + xOffs, y2 + mc.fontRendererObj.FONT_HEIGHT + yOffs, 1342177280);
			mc.fontRendererObj.drawString(name, x + xOffs, y2 + yOffs, 553648127);
			mc.fontRendererObj.drawString(points, x2 - mc.fontRendererObj.getStringWidth(points) + xOffs, y2 + yOffs, 553648127);
			if (index == scores.size()) {
				String displayName = objective.getDisplayName();
				Gui.drawRect(x - 2 + xOffs, y2 - mc.fontRendererObj.FONT_HEIGHT - 1 + yOffs, x2 + xOffs, y2 - 1 + yOffs, 1610612736);
				Gui.drawRect(x - 2 + xOffs, y2 - 1 + yOffs, x2 + xOffs, y2 + yOffs, 1342177280);
				mc.fontRendererObj.drawString(displayName, x + width / 2 - mc.fontRendererObj.getStringWidth(displayName) / 2 + xOffs, y2 - mc.fontRendererObj.FONT_HEIGHT + yOffs, 553648127);
			}
		}
	}
	
	@Override
	public int getX() {
		return x + xOffs;
	}

	@Override
	public int getY() {
		return y + yOffs;
	}

	@Override
	public int getRawWidth() {
		return width;
	}

	@Override
	public int getRawHeight() {
		return height;
	}
	
	@Override
	public void setX(int x) {
		xOffs = x - this.x;
	}
	
	@Override
	public void setY(int y) {
		yOffs = y - this.y;
	}
	
	@Override
	public boolean isVisible() {
		return super.isVisible() && width != 0 && height != 0;
	}
}
