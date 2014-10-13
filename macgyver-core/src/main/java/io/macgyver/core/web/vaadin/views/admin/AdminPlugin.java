/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.macgyver.core.web.vaadin.views.admin;

import io.macgyver.core.Plugin;
import io.macgyver.core.web.vaadin.MacGyverUI;

import com.vaadin.server.VaadinServletService;

public class AdminPlugin extends Plugin {

	@Override
	public void registerViews(MacGyverUI ui) {

		String user = VaadinServletService.getCurrentServletRequest()
				.getRemoteUser();


		
		ui.registerView(PropertyEncryptionView.class);
		ui.registerView(ScriptsView.class);
		ui.registerView(BeansView.class);
		ui.registerView(ClusterView.class);
		ui.registerView(ServicesView.class);
		
	}



}
