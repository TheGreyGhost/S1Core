/**
 * This package is all public 'api'. Anything here is less library based, and more based more on modding.
 * This allows interfaces, methods, etc, to be stripped at runtime through use of {@code @Optional.*} annotations.
 * <br></br>
 * The {@link S1CoreAPI} class contains methods intended to be used by other mods, as these methods are guaranteed
 * not to change on short notice.
 * <br></br>
 * The API contract:
 * <br></br>
 * <ol>
 *     <li>Deprecation Phases</li>
 *     <ol>
 *         <li>
 *         Available: This applies to most things included in this API. If something is 'available', it is the
 *         'recommended' way of doing something. Something marked as 'Available' cannot be removed without going
 *         through the deprecation cycle.
 *         </li>
 *         <li>
 *         Beta: This applies to anything that is marked with a {@code @Beta} annotation. If something is marked
 *         as Beta, it can be changed or removed at any time. If a class file is marked as beta, both the class
 *         and any material contained by the class should be assumed as Beta. Material marked as 'Beta' could also
 *         be relatively safe to use - look for any JavaDoc to see any further information. Reasons something may
 *         be marked as 'Beta' include:
 *             <ul>
 *                 <li>Instability</li>
 *                 <li>Known bugs</li>
 *                 <li>New/Developing Idea</li>
 *             </ul>
 *         </li>
 *         <li>
 *         Deprecated: This applies to anything that is marked with a {@code @Deprecated} annotation. If something
 *         from the available state is going to be removed, it will be marked as deprecated for the duration of:
 *             <ul>
 *                 <li>Two weeks - if the feature is small, or relatively unused,</li>
 *                 <li>The next minor version change of the API, if the feature is relatively small,
 *                 but requires medium to large changes of code, or</li>
 *                 <li>The next major version change of the API, if the feature is large and/or requires a large
 *                 amount of change in code.</li>
 *             </ul>
 *         Material marked as 'Deprecated' will always also contain an alternative and an explanation for
 *         deprecation, as well as the deprecation date. Please note that there is another reason for the use
 *         of the {@code @Deprecated} annotation, which is on material that should not be accessed as it
 *         is there for internal purposes only.
 *         </li>
 *     </ol>
 * </ol> 
 */
@API(apiVersion = "1.0", owner = S1Core.MOD_ID, provides = "S1CORE|API")
package com.shieldbug1.core.api;
import net.minecraftforge.fml.common.API;

import com.shieldbug1.core.S1Core;

