/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.wisc.my.portlets.bookmarks.web;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.mvc.SimpleFormController;

import edu.wisc.my.portlets.bookmarks.dao.BookmarkStore;
import edu.wisc.my.portlets.bookmarks.domain.Bookmark;
import edu.wisc.my.portlets.bookmarks.domain.BookmarkSet;
import edu.wisc.my.portlets.bookmarks.domain.CollectionFolder;
import edu.wisc.my.portlets.bookmarks.domain.Folder;
import edu.wisc.my.portlets.bookmarks.domain.Preferences;
import edu.wisc.my.portlets.bookmarks.web.support.BookmarkSetRequestResolver;
import edu.wisc.my.portlets.bookmarks.web.support.PreferencesRequestResolver;
import edu.wisc.my.portlets.bookmarks.web.support.ViewConstants;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 12168 $
 */
public class BaseBookmarksFormController extends SimpleFormController {
    private String handlerMappingParameter = "action";

    protected BookmarkSetRequestResolver bookmarkSetRequestResolver;
    protected PreferencesRequestResolver preferencesRequestResolver;
    protected BookmarkStore bookmarkStore;
    

    
    /**
     * @return Returns the handlerMappingParameter.
     */
    public String getHandlerMappingParameter() {
        return this.handlerMappingParameter;
    }

    /**
     * @param handlerMappingParameter The handlerMappingParameter to set.
     */
    public void setHandlerMappingParameter(String handlerMappingParameter) {
        this.handlerMappingParameter = handlerMappingParameter;
    }

    /**
     * @return Returns the bookmarkSetRequestResolver.
     */
    public BookmarkSetRequestResolver getBookmarkSetRequestResolver() {
        return this.bookmarkSetRequestResolver;
    }

    /**
     * @param bookmarkSetRequestResolver The bookmarkSetRequestResolver to set.
     */
    public void setBookmarkSetRequestResolver(BookmarkSetRequestResolver bookmarkSetRequestResolver) {
        this.bookmarkSetRequestResolver = bookmarkSetRequestResolver;
    }

    /**
     * @return Returns the bookmarkStore.
     */
    public BookmarkStore getBookmarkStore() {
        return this.bookmarkStore;
    }

    /**
     * @param bookmarkStore The bookmarkStore to set.
     */
    public void setBookmarkStore(BookmarkStore bookmarkStore) {
        this.bookmarkStore = bookmarkStore;
    }

    /**
     * @return Returns the preferencesRequestResolver.
     */
    public PreferencesRequestResolver getPreferencesRequestResolver() {
        return this.preferencesRequestResolver;
    }

    /**
     * @param preferencesRequestResolver The preferencesRequestResolver to set.
     */
    public void setPreferencesRequestResolver(PreferencesRequestResolver preferencesRequestResolver) {
        this.preferencesRequestResolver = preferencesRequestResolver;
    }


    /**
     * @see org.springframework.web.portlet.mvc.SimpleFormController#referenceData(javax.portlet.PortletRequest, java.lang.Object, org.springframework.validation.Errors)
     */
    @Override
    protected Map referenceData(PortletRequest request, Object command, Errors errors) throws Exception {
        final BookmarkSet bookmarkSet = this.bookmarkSetRequestResolver.getBookmarkSet(request, false);
        final Preferences preferences = this.preferencesRequestResolver.getPreferences(request, false);

        final Map<String, Object> refData = new HashMap<String, Object>();
        refData.put(ViewConstants.BOOKMARK_SET, bookmarkSet);
        refData.put(ViewConstants.ERRORS, errors);
        
        if (preferences != null) {
            refData.put(ViewConstants.OPTIONS, preferences);
        }
        else {
            refData.put(ViewConstants.OPTIONS, new Preferences());
        }

        refData.put(ViewConstants.COMMAND_EMPTY_BOOKMARK, new Bookmark());
        refData.put(ViewConstants.COMMAND_EMPTY_FOLDER, new Folder());
        refData.put(ViewConstants.COMMAND_EMPTY_COLLECTION, new CollectionFolder());

        return refData;
    }

    /**
     * @see org.springframework.web.portlet.mvc.SimpleFormController#processFormSubmission(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected void processFormSubmission(ActionRequest request, ActionResponse response, Object command, BindException errors) throws Exception {
    	
    	// don't save preferences for guest users
    	if (request.getRemoteUser() == null)
    		return;
    	
        super.processFormSubmission(request, response, command, errors);

        if (errors.getErrorCount() <= 0) {
            response.setRenderParameter(this.handlerMappingParameter, this.getSuccessView());
        }
    }
    
}
