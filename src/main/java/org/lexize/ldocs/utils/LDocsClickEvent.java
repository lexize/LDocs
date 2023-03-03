package org.lexize.ldocs.utils;

import net.minecraft.network.chat.ClickEvent;

public class LDocsClickEvent extends ClickEvent {
    public final Runnable onClickHandler;
    public LDocsClickEvent(Runnable onClickHandler) {
        super(Action.SUGGEST_COMMAND, "");
        this.onClickHandler = onClickHandler;
    }
}
