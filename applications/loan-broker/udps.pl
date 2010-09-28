#!/usr/bin/perl -w
#Perl Script to catch multicast packets.
use strict;
use IO::Socket::Multicast;

my $socket = IO::Socket::Multicast->new(LocalPort=>11111, ReuseAddr=>1)
  or die "Can't start UDP server: $@";

$socket->mcast_add('225.6.7.8');

my ($datagram,$flags);
while ($socket->recv($datagram,1024,$flags)) {
	print "**Shark** $datagram\n";
}

$socket->close();
