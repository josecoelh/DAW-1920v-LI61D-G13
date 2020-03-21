DROP TABLE IF EXISTS _COMMENTS;
DROP TABLE IF EXISTS ISSUE_LABELS;
DROP TABLE IF EXISTS ISSUES;
DROP TABLE IF EXISTS ALLOWED_LABELS;
DROP TABLE IF EXISTS PROJECT;

CREATE TABLE PROJECT(
	proj_id UUID,
	_NAME VARCHAR(30),
	DESCRIPTION VARCHAR(100),
	PRIMARY KEY(proj_id)
);

CREATE TABLE ALLOWED_LABELS(
	_value VARCHAR(50),
	proj_id UUID,
	FOREIGN KEY (proj_id) REFERENCES PROJECT(proj_id),
	PRIMARY KEY(_value, proj_id)
);

CREATE TABLE ISSUES(
	_name VARCHAR(50),
	issue_id UUID,
	proj_id UUID,
	_state VARCHAR(8),
	FOREIGN KEY (proj_id) REFERENCES PROJECT(proj_id),
	PRIMARY KEY(issue_id)
);

CREATE TABLE ISSUE_LABELS(
	_value VARCHAR(50),
	issue_id UUID,
	FOREIGN KEY (issue_id) REFERENCES ISSUES(issue_id),
	PRIMARY KEY(_value, issue_id)
);

CREATE TABLE _COMMENTS(
	_comment VARCHAR(300),
	_DATE VARCHAR(16),
	comment_id UUID,
	issue_id UUID,
	FOREIGN KEY (issue_id) REFERENCES ISSUES(issue_id),
	PRIMARY KEY(comment_id)
);

/* INSERT TEST VALUES*/
insert  into  project values ('3779a41b-7920-4f9c-b3aa-75d9302a1abe','cena do ','teste fudido');
insert into allowed_labels values ('v','3779a41b-7920-4f9c-b3aa-75d9302a1abe');
insert into allowed_labels values ('vv','3779a41b-7920-4f9c-b3aa-75d9302a1abe');
insert into issues values ('issueTest', '3779a41b-7920-4f9c-b3aa-75d9302a2abe','3779a41b-7920-4f9c-b3aa-75d9302a1abe','CLOSED');
insert into _comments values ('teste','04/02/2020 04 03','3779a41b-7920-4f9c-b3aa-75d9302a2abe','3779a41b-7920-4f9c-b3aa-75d9302a2abe');
insert into _comments values ('teste','04/02/2020 04 03','9611db05-b2eb-449d-87dc-427bf8f3944f','3779a41b-7920-4f9c-b3aa-75d9302a2abe');
insert into _comments values ('teste','04/02/2020 04 03','9611db05-b2eb-449d-87dc-427bf8f3944a','3779a41b-7920-4f9c-b3aa-75d9302a2abe');
insert into issue_labels values ('v','3779a41b-7920-4f9c-b3aa-75d9302a2abe');
insert into issue_labels values ('vv','3779a41b-7920-4f9c-b3aa-75d9302a2abe');
