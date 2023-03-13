// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.crypto;

import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Set;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class SecurityProviderApp {

    public static void main(String[] args) {
	try {
	    System.out.println(String.format("***** Java Runtime Version: %s *****", Runtime.version().toString()));
	    System.out.println("***** Installed Security Providers *****");
	    System.out.println();

	    int seq = 0;
	    for (Provider p : Security.getProviders()) {

		System.out.println(String.format("[%02d] %s - Version %s", (++seq), p.getName(), p.getVersionStr()));
		System.out.println(p.getInfo());
		System.out.println();

		Set<Service> services = p.getServices();
		if (!services.isEmpty()) {
		    System.out.println("* Services:");

		    int subseq = 0;
		    for (Service service : services) {

			System.out.println(String.format("(%02d) Algorithm: %s", (++subseq), service.getAlgorithm()));
			System.out.println(service);
		    }
		}
	    }

	    System.out.println("***** Done *****");

	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}
