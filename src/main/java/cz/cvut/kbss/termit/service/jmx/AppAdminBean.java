/**
 * TermIt
 * Copyright (C) 2019 Czech Technical University in Prague
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package cz.cvut.kbss.termit.service.jmx;

import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.termit.event.RefreshLastModifiedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "bean:name=TermItAdminBean", description = "TermIt administration JMX bean.")
public class AppAdminBean {

    private static final Logger LOG = LoggerFactory.getLogger(AppAdminBean.class);

    private final ApplicationEventPublisher eventPublisher;

    private final EntityManagerFactory emf;

    @Autowired
    public AppAdminBean(ApplicationEventPublisher eventPublisher, EntityManagerFactory emf) {
        this.eventPublisher = eventPublisher;
        this.emf = emf;
    }

    @ManagedOperation(description = "Invalidates the application caches.")
    public void invalidateCaches() {
        LOG.info("Invalidating application caches...");
        emf.getCache().evictAll();
        LOG.info("Refreshing last modified timestamps...");
        eventPublisher.publishEvent(new RefreshLastModifiedEvent(this));
    }
}
