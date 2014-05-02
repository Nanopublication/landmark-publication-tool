
    /*
     * Styling
     */
    $(':input').addClass('ui-corner-all'); // all input fields with rounded corners
    $('#detailsText').hide();
    $('#detailsAction').click(function() {
        $('#detailsQuestion').hide();
        $('#detailsText').show();
    });

    $('#detailsArea').focus(function() {
        if (this.value === this.defaultValue) {
            this.value = '';
        }
    }).blur(function() {
        if (this.value === '') {
            this.value = this.defaultValue;
        }
    });

    /*
     * Check input before submission 
     * TODO: used to be in client, to be moved to server
     */


    /*
     * Accordion: a single html FORM which is moved to the currently opened accordion tab
     */
    $('#accordion').accordion();
    $('.acc-header').click(moveForm);
    function moveForm() {
        $(this).next().append($('#inputForm'));
        updateTags();
        updateTips(getConceptQTips(tags[0], tags[1]), clickable);
        resetForm();
        console.log(tags);
    }
    

    function resetForm() {
        conceptEditable('#subject');
        conceptEditable('#object');
        $('input').filter('[type=text]').val('');
//                    validator.resetForm();
    }


    /*
     * Keep "tags" and subject/object field labels up-to-date with accordion selection
     */
    var tags = "";
    function updateTags() {
        tags = $('#inputForm').parent().prev().children('a').text().split(/\s+\-\s+/);
        $("#subjectLabel").html(tags[0]);
        $("#objectLabel").html(tags[1]);
    }
    updateTags();




    /*
     * After selecting a concept with autocomplete, it becomes immutable
     *  until the 'remove' button is clicked
     */
    function conceptImmutable(c) {
        $(c + ' > .validateConcept').attr('readonly', true)
                .addClass('immutable');
        $(c + ' > .remove').show();
        console.log($(c + ' > .hidden').val());
        if ($(c + ' > .hidden').val()) { // unfocusing from autocomplete selection will leave invalid concept (so do not show CW link)
            $(c + ' > .cwinfo').show();
        }
    }

    function conceptEditable(c) {
        $(c + ' > .validateConcept').attr('readonly', false)
                .removeClass('immutable')
                .attr('value', '');
        $(c + ' > .hidden').val('');
        $(c + ' > .remove').hide();
        $(c + ' > .cwinfo').hide();
    }

    // make sub/obj concepts editable on page load and on 'remove' button
    conceptEditable('#subject');
    $('#subject > .remove').click(function() {
        conceptEditable('#subject');
    });
    conceptEditable('#object');
    $('#object > .remove').click(function() {
        conceptEditable('#object');
    });


    /*
     * Date selector
     */
    $("#datepicker").datepicker({dateFormat: "yy-mm-dd", changeYear: true, yearRange: "c-50:c"});


    /*
     * Qtip popups with extra information about fields
     */
    var clickable = {show: 'click', hide: 'unfocus'};

    function updateTips(tipList, params) {
        jQuery.each(tipList, function(elt, msg) {
            $(elt).qtip(jQuery.extend({content: msg}, params));
        });
    }

    function getConceptQTips(subjectTag, objectTag) {
        return {
            '#subject > .info': {
                text: 'Select a ' + subjectTag + ' concept as the first part of your claim',
                title: subjectTag + ' (autocomplete)'
            },
            '#object > .info': {
                text: 'Select a ' + objectTag + ' concept as the second part of your claim',
                title: objectTag + ' (autocomplete)'
            }
        };
    }

    var staticQTips = {
        '#cidInfo': {
            title: 'Original Author / Researcher (optional)',
            text: 'List one of the original authors of the publication. Enter name or, preferably, a Researcher ID (get one <a href="http://www.researcherid.com/Home.action">here</a>).'
        },
        '#pdateInfo': {
            title: 'Publication date',
            text: 'When was the landmark paper published?'
        },
        '#doiInfo': {
            title: 'Landmark paper',
            text: 'Please specify the DOI or PubMedID of the <strong>first</strong> paper that lists this association.<br>For example: <pre>doi:10.1000/182</pre> or <pre>9454348</pre>'
        },
        '#ridInfo': {
            title: 'Curator (optional)',
            text: 'List the curator of this landmark claim: you! Enter name or, preferably, a Researcher ID (get one <a href="http://www.researcherid.com/Home.action">here</a>).'
        }
    };

    var staticQTipsHover = {
        '.remove': {
            title: 'Undo',
            text: 'Erase this concept and enter a new one'
        },
        '.cwinfo': {
            title: 'Not sure this is the right concept?',
            text: 'Disambiguate by viewing this concept in the conceptwiki'
        },
        '#analyticalEnv': {
            text: 'result of analytical derivation, verified by lab experiment',
        },
        '#accidentalEnv': {
            text: 'serendipitous result, for example found in the course of different experiment'
        }
    };

    // initialize qtips
    updateTips(getConceptQTips(tags[0], tags[1]), clickable);
    updateTips(staticQTips, clickable);
    updateTips(staticQTipsHover, {});


/*
 * Autocomplete
 * only for concepts, institution autocomplete not yet implemented
 */

var templateConceptMap = {
    Gene: "a3b5c57e-8ac1-46ac-afef-3347d40c4d37", // Gene or Genome
    Protein: "eeaec894-d856-4106-9fa1-662b1dc6c6f1", // Amino Acid, Peptide, or Protein
    Disease: "eda73945-b112-407e-811a-88448966834f", // Disease or Syndrome
    Drug: "07a84994-e464-4bbf-812a-a4b96fa3d197", // Chemical Viewed Structurally
    chem: "07a84994-e464-4bbf-812a-a4b96fa3d197", // Chemical Viewed Structurally
    effect: "52f93d41-bd3a-4c6a-b289-ac7c3d362630", // Sign or Symptom
    func: "43c48c2d-6c09-4028-aa2c-ac1b2b296641"  // Genetic Function
};

//$("#inst").autocomplete({
//    position: {my: "left top", at: "left bottom"},
//    source:
//            function(request, response) {
//                console.log("inst autocomplete for: " + request.term);
//
//                $.ajax({
//                    url: "http://127.0.0.1:8181/npvalidator/validator/landmark/autocomplete/" + request.term,
//                    dataType: "jsonp",
//                    //contentType: "application/json",
//                    data: {
//                        q: request.term
//                    },
//                    success: function(data) {
//                        console.log(data);
//                        response(data);
//                    },
//                    error: function(jqXHR, textStatus, errorThrown) {
//                        alert(textStatus);
//                        alert(errorThrown);
//                    }
//                });
//            },
//});

function cwAutoComplete(conceptType) { // conceptType = { '#subject' | '#object' }
	console.log("setting autocomplete on " + conceptType);
	console.log($('#subject'));
    $(conceptType + ' > .validateConcept').autocomplete({
        position: {my: "left top", at: "left bottom"},
        source: function(request, response) {
            var tag;
            if (conceptType == "#subject") {
                tag = templateConceptMap[tags[0]];
            } else {
                tag = templateConceptMap[tags[1]];
            }
            console.log(tag);

            $.ajax({
                url: "http://www.conceptwiki.org/web-ws/concept/search/byTag",
                dataType: "jsonp",
                data: {
                    q: request.term,
                    uuid: tag
                },
                success: function(data) {
                    response($.map(data, function(item) {
                        console.log(item);
//                        
//                        if(request.match == "No highlight information available") {
//                        	return {
//                                label: request.term,
//                                value: request.term + " uuid=" + item.uuid
//                            };
//                        }
                        
                        var label = item.match;
                        //var label = item.labels[0].text;
                        var value = label;
                        
                        for( var i=0; i<item.labels.length; i++) {
                        	var re = new RegExp(request.term, "i");
                        	var Label = item.labels[i].text;
                        	console.log("trying to match: " + Label);
                        	if(Label.match(re)) {
                        		console.log("match: " + Label);
                        		return {
                        			label: Label,
                        		    value: Label + " uuid=" + item.uuid
                        		};
                        	};
                        };

                        console.log("Could not find a label that matched request term " + request.term);
                        return {
                        	label: request.term,
                        	value: item.labels[0].text + " uuid=" + item.uuid
                        };
                        
                        
//                        // TODO: make <em> substitution global (if multiple matches)
//                        if(label == "No highlight information available") {
//                        	label = value = request.term;
//                        } else {
//                            //label = label.replace(/<em>(.*?)<\/em>/g, ""); // search results seem to repeat <em> highlighted parts, so removing them
//                            label = label.replace("<em>", "");
//                        	label = label.replace("</em>", "");
//                        }
//                        value = value + " uuid=" + item.uuid;
//                        //value = value.replace(/<em>(.*?)<\/em>/g, "");
//                        value = value.replace("<em>", "");
//                        value = value.replace("</em>", "");
//
//                        return {
//                            label: label,
//                            value: value
//                        };
                    }));
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    alert(errorThrown);
                }
            });
        },
        html: true,
        minLength: 3,
        close: function() {
            var url = 'http://www.conceptwiki.org/concept/index/';
            var parts = $(conceptType + ' > .validateConcept').val().split(" uuid=");
            $(conceptType + ' > .cwinfo').attr('href', url + parts[1]);
            $(conceptType + ' > .validateConcept').val(parts[0]);

            $(conceptType + " > .hidden").val(parts[1]); // set hidden uuid field

            conceptImmutable(conceptType);
            //TODO: (now undefined) validator below caused concept input to unfocus?
            //validator.element($(conceptType + ' > .validateConcept')); // in case this selection is a redo: verify selection now
        }
    });
    }; 

    // show ConceptWiki in a pop-up fancybox (cross-site error given, but page is shown!)
    $('.cwinfo').fancybox({'width': '80%',
        'height': '80%',
        'type': 'iframe',
        'headers'  : { 'X-fancyBox': true },
        'closeClick': true,
        'closeBtn': true});
    
    // initialize autocomplete
    cwAutoComplete("#subject");
    cwAutoComplete("#object");



