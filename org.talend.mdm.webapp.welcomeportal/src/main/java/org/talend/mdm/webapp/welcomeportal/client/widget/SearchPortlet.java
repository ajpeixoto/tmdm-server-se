// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.webapp.welcomeportal.client.widget;

import org.talend.mdm.webapp.welcomeportal.client.MainFramePanel;
import org.talend.mdm.webapp.welcomeportal.client.WelcomePortal;
import org.talend.mdm.webapp.welcomeportal.client.i18n.MessagesFactory;
import org.talend.mdm.webapp.welcomeportal.client.resources.icon.Icons;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.custom.Portal;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextBox;

public class SearchPortlet extends BasePortlet {

    public SearchPortlet(Portal portal) {
        super(WelcomePortal.SEARCH, portal);

        init();
    }

    @Override
    protected void initAutoRefresher() {
        return;
    }

    @Override
    public void refresh() {
        // stay the same, no need to refresh
        return;
    }

    private void init() {
        set.setBorders(false);

        Grid grid = new Grid(1, 2);
        final TextBox textBox = new TextBox();
        textBox.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent keyUpEvent) {
                if (keyUpEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    doSearch(textBox);
                }
            }
        });
        grid.setWidget(0, 0, textBox);

        Button button = new Button(MessagesFactory.getMessages().search_button_text());
        button.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                doSearch(textBox);
            }
        });

        grid.setWidget(0, 1, button);

        set.add(grid);
        set.layout(true);

    }

    @Override
    public void setHeading() {
        this.setHeading(MessagesFactory.getMessages().search_title());
    }

    @Override
    public void setIcon() {
        this.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.find()));
    }

    private void doSearch(TextBox textBox) {
        // TODO TMDM-2598 Temp code (how to pass a parameter to an application?).
        if (Cookies.isCookieEnabled()) {
            Cookies.setCookie("org.talend.mdm.search.query", textBox.getText()); //$NON-NLS-1$
        }
        ((MainFramePanel) portal).itemClick("search", "Search"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
