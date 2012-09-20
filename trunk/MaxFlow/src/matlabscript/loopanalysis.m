cd D:\PhDWork\Jspace\MaxFlow\test\loopanalysis;
%conSet = load('test-config.txt');
p=1;
for i = [200]
        f = 'Grag_Random_Run.txt';
        C = load(f);
        N = C(:,1);
        CR = C(:,3);   
        f = 'Wf_Random_Run.txt';
        W = load(f);
        WR = W(:,3);
        plot(N,CR,'r',N,WR,'b');
        legend('Garg','TPath',2);
        %plot(N,CR,'r',N,WR,'b');
        xlabel('Total Number of Nodes');
        ylabel('Times of ShortPath Function');
        %saveas(gcf,v,'pdf');
        p = p+1;
    end
    v = 'loopanalysis';
    saveas(gcf,v,'pdf');
