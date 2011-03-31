/*  Copyright 2011 Fabrizio Cannizzo
 *
 *  This file is part of RestFixture.
 *
 *  RestFixture (http://code.google.com/p/rest-fixture/) is free software:
 *  you can redistribute it and/or modify it under the terms of the
 *  GNU Lesser General Public License as published by the Free Software Foundation,
 *  either version 3 of the License, or (at your option) any later version.
 *
 *  RestFixture is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with RestFixture.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  If you want to contact the author please leave a comment here
 *  http://smartrics.blogspot.com/2008/08/get-fitnesse-with-some-rest.html
 */
package smartrics.rest.fitnesse.fixture;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import smartrics.rest.fitnesse.fixture.support.StringTypeAdapter;

public class SlimFormatterTest {

    @Test
    public void shouldDisplayGrayedActualOnCheckIfNoExpectedIsSpecified() {
        SlimCell c = new SlimCell("");
        SlimFormatter formatter = new SlimFormatter();
        StringTypeAdapter actual = new StringTypeAdapter();
        actual.set("2");
        formatter.check(c, actual);
        assertThat(c.body(), is(equalTo("report:2")));
    }

    @Test
    public void shouldDisplayNothingOnCheckIfNoExpectedIsSpecifiedAndActualIsNullOrEmpty() {
        SlimCell c = new SlimCell("");
        SlimFormatter formatter = new SlimFormatter();
        StringTypeAdapter actual = new StringTypeAdapter();
        formatter.check(c, actual);
        assertThat(c.body(), is(equalTo("")));
        actual.set("");
        assertThat(c.body(), is(equalTo("")));
    }

    @Test
    public void shouldDisplayPassOnCheckIfExpectedAndActualMatch() {
        SlimCell c = new SlimCell("abc123");
        SlimFormatter formatter = new SlimFormatter();
        StringTypeAdapter actual = new StringTypeAdapter();
        actual.set("abc123");
        formatter.check(c, actual);
        assertThat(c.body(), is(equalTo("pass:abc123")));
    }

    @Test
    public void shouldDisplayPassOnCheckIfExpectedAndActualMatch_whenDisplayingActual() {
        SlimCell c = new SlimCell("something matching logically abc123");
        SlimFormatter formatter = new SlimFormatter();
        formatter.setDisplayActual(true);
        // mockito seems not to be able to correctly allow
        // StringTypeAdapter actual = mock(StringTypeAdapter.class);
        // when(actual).equals(isA(Object.class, Object.class)).thenReturn(true)
        // as its not picking up the overridden method but
        // Object.equals(Object).
        StringTypeAdapter actual = new StringTypeAdapter() {
            @Override
            public boolean equals(Object a, Object b) {
                return true;
            }
        };
        actual.set("abc123");
        formatter.check(c, actual);
        assertThat(c.body(), is(equalTo("pass:something&nbsp;matching&nbsp;logically&nbsp;abc123<br/><i>expected</i><hr/><br/>abc123<br/><i>actual</i>")));
    }

    @Test
    public void shouldDisplayFailOnCheckIfExpectedAndActualMatch_whenNotDisplayingActual() {
        SlimCell c = new SlimCell("abc123");
        SlimFormatter formatter = new SlimFormatter();
        formatter.setDisplayActual(false);
        StringTypeAdapter actual = new StringTypeAdapter();
        actual.set("def345");
        formatter.check(c, actual);
        assertThat(c.body(), is(equalTo("fail:abc123<br/><i>expected</i>")));
    }

    @Test
    public void shouldDisplayFailOnCheckIfExpectedAndActualMatch_whenDisplayingActual() {
        SlimCell c = new SlimCell("abc123");
        SlimFormatter formatter = new SlimFormatter();
        formatter.setDisplayActual(true);
        StringTypeAdapter actual = new StringTypeAdapter();
        actual.set("def345");
        formatter.check(c, actual);
        assertThat(c.body(), is(equalTo("fail:abc123<br/><i>expected</i><hr/><br/>def345<br/><i>actual</i>")));
    }

}